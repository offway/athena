package cn.offway.athena.controller;

import cn.offway.athena.domain.PhBrand;
import cn.offway.athena.domain.PhFeedback;
import cn.offway.athena.domain.PhFeedbackDetail;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.PhBrandService;
import cn.offway.athena.service.PhFeedbackDetailService;
import cn.offway.athena.service.PhFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class FeedbackController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhFeedbackService feedbackService;
    @Autowired
    private PhFeedbackDetailService feedbackDetailService;
    @Autowired
    private PhBrandService brandService;

    @RequestMapping("/feedback.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "feedback_index";
    }

    @ResponseBody
    @RequestMapping("/feedback_list")
    public Map<String, Object> getList(int sEcho, int iDisplayStart, int iDisplayLength) {
        Sort sort = new Sort("id");
        Page<PhFeedback> pages = feedbackService.findAll(new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @RequestMapping("/feedback_detail.html")
    public String index(ModelMap map, Long id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        PhFeedback obj = feedbackService.findOne(Long.valueOf(id));
        if (obj != null) {
            map.addAttribute("theName", obj.getBrandName());
        }
        return "feedback_detail";
    }

    @ResponseBody
    @RequestMapping("/feedback_detail_list")
    public Map<String, Object> getList(int sEcho, int iDisplayStart, int iDisplayLength, long id) {
        Sort sort = new Sort("id");
        Page<PhFeedbackDetail> pages = feedbackDetailService.findByPid(id, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @RequestMapping("/feedback_modify.html")
    public String modify(ModelMap map, String id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", id);
        return "feedback_compose";
    }

    @RequestMapping("/feedback_insert.html")
    public String insert(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", "XYZ");
        return "feedback_compose";
    }

    @ResponseBody
    @RequestMapping("/feedback_detail_save")
    @Transactional
    public boolean save(PhFeedbackDetail detail, @RequestParam("image") String[] images, String action) {
        if ("add".equals(action)) {
            PhBrand brand = brandService.findOne(detail.getBrandId());
            if (brand != null) {
                detail.setBrandLogo(brand.getLogo());
                detail.setBrandName(brand.getName());
                detail.setBackTime(new Date());
                detail.setImgNum((long) images.length);
                detail.setImgUrl(String.join(",", images));
                PhFeedback feedback = feedbackService.findByBrandId(brand.getId());
                if (feedback != null) {
                    feedback.setImgNum(feedback.getImgNum() + images.length);
                    long num = feedbackDetailService.checkStarName(feedback.getId(), detail.getStarName());
                    if (num == 0) {
                        feedback.setStarNum(feedback.getStarNum() + 1);
                    }
                } else {
                    feedback = new PhFeedback();
                    feedback.setImgNum((long) images.length);
                    feedback.setBrandId(brand.getId());
                    feedback.setBrandLogo(brand.getLogo());
                    feedback.setBrandName(brand.getName());
                    feedback.setStarNum(1L);
                }
                PhFeedback feedbackSaved = feedbackService.save(feedback);
                detail.setPid(feedbackSaved.getId());
                feedbackDetailService.save(detail);
            }
        } else {
            PhFeedbackDetail feedbackDetailFull = feedbackDetailService.findOne(detail.getId());
            PhFeedback feedback = feedbackService.findOne(detail.getPid());
            feedback.setImgNum(feedback.getImgNum() - feedbackDetailFull.getImgNum() + images.length);
            if (!detail.getStarName().equals(feedbackDetailFull.getStarName())) {
                long oldNum = feedbackDetailService.checkStarName(feedbackDetailFull.getPid(), feedbackDetailFull.getStarName());
                long newNum = feedbackDetailService.checkStarName(feedbackDetailFull.getPid(), detail.getStarName());
                if (oldNum == 1) {
                    feedback.setStarNum(feedback.getStarNum() - 1);
                }
                if (newNum == 0) {
                    feedback.setStarNum(feedback.getStarNum() + 1);
                }
            }
            feedbackDetailFull.setStarName(detail.getStarName());
            feedbackDetailFull.setWeibo(detail.getWeibo());
            feedbackDetailFull.setImgNum((long) images.length);
            feedbackDetailFull.setImgUrl(String.join(",", images));
            feedbackDetailService.save(feedbackDetailFull);
            feedbackService.save(feedback);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/feedback_detail_get")
    public PhFeedbackDetail get(Long id) {
        return feedbackDetailService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/feedback_del")
    public boolean deleteAll(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            feedbackService.delete(id);
            feedbackDetailService.delByPid(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/feedback_detail_del")
    @Transactional
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            PhFeedbackDetail detail = feedbackDetailService.findOne(id);
            PhFeedback feedback = feedbackService.findOne(detail.getPid());
            feedback.setImgNum(feedback.getImgNum() - detail.getImgNum());
            long unique = feedbackDetailService.checkStarName(detail.getPid(), detail.getStarName());
            if (unique == 1) {
                feedback.setStarNum(feedback.getStarNum() - 1);
            }
            feedbackService.save(feedback);
            feedbackDetailService.delete(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/feedback_brand_list")
    public List<PhBrand> getBrandList() {
        return brandService.findAll();
    }
}
