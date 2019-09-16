package cn.offway.athena.controller;

import cn.offway.athena.domain.PhBanner;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.PhBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class BannerController {
    @Autowired
    private PhBannerService bannerService;
    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/banner.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "banner_index";
    }

    @RequestMapping("/banner_list")
    @ResponseBody
    public Map<String, Object> getAll(HttpServletRequest request, String id, String remark) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "status"), new Sort.Order("sort"));
        Page<PhBanner> pages = bannerService.findAll(id, remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @RequestMapping("/banner_save")
    @ResponseBody
    public boolean save(PhBanner banner) {
        if (banner.getId() == null) {
            banner.setStatus("0");
            banner.setSort(null);
            banner.setCreateTime(new Date());
        } else {
            PhBanner bannernew = bannerService.findOne(banner.getId());
            banner.setStatus(bannernew.getStatus());
            banner.setSort(bannernew.getSort());
            banner.setCreateTime(bannernew.getCreateTime());
        }
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_get")
    @ResponseBody
    public PhBanner get(@RequestParam("id") Long id) {
        return bannerService.findOne(id);
    }

    @RequestMapping("/banner_del")
    @ResponseBody
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            bannerService.delete(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/banner_top")
    public boolean top(Long id, Long sort) {
        PhBanner banner = bannerService.findOne(id);
        if (banner != null) {
            bannerService.resort(sort);
            banner.setSort(sort);
            bannerService.save(banner);
        }
        return true;
    }

    @RequestMapping("/banner_up")
    @ResponseBody
    public boolean up(Long id) {
        PhBanner banner = bannerService.findOne(id);
        long latest = bannerService.getMaxSort();
        banner.setStatus("1");
        banner.setSort(latest + 1);
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_down")
    @ResponseBody
    public boolean down(Long id) {
        PhBanner banner = bannerService.findOne(id);
        banner.setStatus("0");
        banner.setSort(null);
        bannerService.save(banner);
        return true;
    }
}
