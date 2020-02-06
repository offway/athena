package cn.offway.athena.controller;

import cn.offway.athena.domain.*;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 衣柜审核
 *
 * @author wn
 */
@Controller
public class WardrobeAuditController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhOfflineOrdersService offlineOrdersService;
    @Autowired
    private PhOfflineOrdersGoodsService offlineOrdersGoodsService;
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhOrderInfoService orderInfoService;
    @Autowired
    private PhGoodsService goodsService;
    @Autowired
    private PhGoodsImageService goodsImageService;
    @Autowired
    private PhGoodsStockService goodsStockService;
    @Autowired
    private PhOfflineRemarkService offlineRemarkService;
    @Autowired
    private PhBrandService brandService;

    @RequestMapping("/offline.html")
    public String order(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", "XYZ");
        return "offline";
    }

    @ResponseBody
    @RequestMapping("/offline_brand_list")
    public List<PhBrand> getBrandList() {
        return brandService.findAll();
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("/offline-data")
    public Map<String, Object> offlineData(HttpServletRequest request, String realName, String users, String state, String ordersNo,String brandName, String sTime, String eTime) {

        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));

        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName);
        Date sTimeDate = null, eTimeDate = null;
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(sTime)) {
            sTimeDate = DateTime.parse(sTime, format).toDate();
        }
        if (StringUtils.isNotBlank(eTime)) {
            eTimeDate = DateTime.parse(eTime, format).toDate();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Page<PhOfflineOrders> pages = offlineOrdersService.findByPage(realName, users, state, ordersNo, sTimeDate, eTimeDate, brandName, pr);
        List<Object> list = new ArrayList<>();
        for (PhOfflineOrders tmp : pages.getContent()) {
            Map m = objectMapper.convertValue(tmp, Map.class);
            m.put("msg", offlineRemarkService.findLatest(tmp.getId()));
            list.add(m);
        }
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", list);//数据集合
        return map;
    }

}
