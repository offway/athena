package cn.offway.athena.controller;

import cn.offway.athena.domain.PhFeedback;
import cn.offway.athena.domain.PhFeedbackDetail;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.PhFeedbackDetailService;
import cn.offway.athena.service.PhFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    public boolean save(PhFeedbackDetail detail) {
        feedbackDetailService.save(detail);
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
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            feedbackDetailService.delete(id);
        }
        return true;
    }
}
