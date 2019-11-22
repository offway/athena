package cn.offway.athena.controller;

import cn.offway.athena.domain.*;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.*;
import org.apache.commons.lang3.StringUtils;
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
 * 订单查询
 *
 * @author wn
 */
@Controller
public class OfflineController {

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
    public Map<String, Object> offlineData(HttpServletRequest request, String realName, String users, String state, String ordersNo, String sTime, String eTime) {

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
        Page<PhOfflineOrders> pages = offlineOrdersService.findByPage(realName, users, state, ordersNo, sTimeDate, eTimeDate, pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
        return map;
    }

    @ResponseBody
    @RequestMapping("/offline-confirm")
    public boolean confirm(Long id, String state) {
        PhOfflineOrders offlineOrders = offlineOrdersService.findOne(id);
        /*0-确认发货,1-确认收货*/
        /*0-未寄出,1-已寄出,2-已寄出/未收回,3-已寄出/已收回*/
        if ("0".equals(state)) {
            offlineOrders.setState("1");
        } else {
            offlineOrders.setState("3");
        }
        offlineOrdersService.save(offlineOrders);
        return true;
    }

    @ResponseBody
    @RequestMapping("/offline-confirm-back")
    @Transactional
    public boolean confirmBack(Long id, @RequestParam("ids[]") Long[] ids) {
        for (long gid : ids) {
            PhOfflineOrdersGoods goods = offlineOrdersGoodsService.findOne(gid);
            if (goods != null) {
                /* 状态:[0-未收回,1-已收回] **/
                goods.setState("1");
                offlineOrdersGoodsService.save(goods);
            }
        }
        PhOfflineOrders offlineOrders = offlineOrdersService.findOne(id);
        if (offlineOrders != null) {
            int a = 0, b = 0;
            for (PhOfflineOrdersGoods goods : offlineOrdersGoodsService.findByordersNo(offlineOrders.getOrdersNo())) {
                if ("0".equals(goods.getState())) {
                    a++;
                } else {
                    b++;
                }
            }
            if (a == 0) {
                /*状态:[0-未寄出,1-已寄出,2-未收回,3-已部分收回,4-已收回]*/
                offlineOrders.setState("4");
            } else if (b == 0) {
                /*状态:[0-未寄出,1-已寄出,2-未收回,3-已部分收回,4-已收回]*/
                offlineOrders.setState("2");
            } else {
                /*状态:[0-未寄出,1-已寄出,2-未收回,3-已部分收回,4-已收回]*/
                offlineOrders.setState("3");
            }
            offlineOrdersService.save(offlineOrders);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/offline-save")
    @Transactional
    public boolean save(PhOfflineOrders offlineOrders, @RequestParam(name = "goodsID", required = false) String[] goodsID, @RequestParam(name = "size", required = false) String[] size, @RequestParam(name = "color", required = false) String[] color,
                        @RequestParam(name = "expressOrderNo", required = false) String[] expressOrderNo, @RequestParam(name = "way", required = false) String[] way,
                        @RequestParam(name = "div_brand", required = false) String[] div_brand, @RequestParam(name = "div_goodsName", required = false) String[] div_goodsName,
                        @RequestParam(name = "div_goodsImg", required = false) String[] div_goodsImg, @RequestParam(name = "div_goodsColor", required = false) String[] div_goodsColor,
                        @RequestParam(name = "div_goodsSize", required = false) String[] div_goodsSize, @RequestParam(name = "div_goodsWay", required = false) String[] div_goodsWay,
                        @RequestParam(name = "div_goodsExpress", required = false) String[] div_goodsExpress, String goodsMode) {
        long goodsCount = 0;
        switch (goodsMode) {
            case "0":
                if (size.length != color.length && color.length != goodsID.length) {
                    return false;
                } else {
                    goodsCount = goodsID.length;
                }
                break;
            case "1":
                if (div_goodsSize.length != div_goodsColor.length && div_goodsColor.length != div_goodsName.length) {
                    return false;
                } else {
                    goodsCount = div_goodsName.length;
                }
                break;
            default:
                return false;
        }
        String orderNo = "";
        List<PhOfflineOrdersGoods> offlineOrdersGoodsList = new ArrayList<>();
        if ("".equals(offlineOrders.getOrdersNo())) {
            orderNo = orderInfoService.generateOrderNo("PH") + "XX";
            offlineOrders.setCreateTime(new Date());
            offlineOrders.setState("0");
            offlineOrders.setOrdersNo(orderNo);
            offlineOrders.setGoodsCount(goodsCount);
            offlineOrders.setMessage("0");
            offlineOrdersService.save(offlineOrders);
        } else {
            orderNo = offlineOrders.getOrdersNo();
            offlineOrders.setGoodsCount(goodsCount);
            offlineOrders.setState("0");
            offlineOrdersService.save(offlineOrders);
            offlineOrdersGoodsService.delbyOrdersNo(orderNo);
        }
        if ("0".equals(goodsMode)) {
            for (int i = 0; i < goodsID.length; i++) {
                PhGoods goods = goodsService.findOne(Long.valueOf(goodsID[i]));
                PhGoodsStock goodsStock = goodsStockService.findOne(Long.valueOf(goodsID[i]));
                PhOfflineOrdersGoods offlineOrdersGoods = new PhOfflineOrdersGoods();
                offlineOrdersGoods.setBrandId(goods.getBrandId());
                offlineOrdersGoods.setBrandLogo(goods.getBrandLogo());
                offlineOrdersGoods.setBrandName(goods.getBrandName());
                offlineOrdersGoods.setColor(color[i]);
                if (StringUtils.isNotBlank(way[i])) {
                    offlineOrdersGoods.setWay(way[i]);
                }
                if (StringUtils.isNotBlank(expressOrderNo[i])) {
                    offlineOrdersGoods.setExpressOrderNo(expressOrderNo[i]);
                } else {
                    offlineOrdersGoods.setExpressOrderNo("无");
                }
                offlineOrdersGoods.setCreateTime(new Date());
                offlineOrdersGoods.setGoodsId(goods.getId());
                offlineOrdersGoods.setGoodsImage(goods.getImage());
                offlineOrdersGoods.setGoodsName(goods.getName());
                if (StringUtils.isNotBlank(orderNo)) {
                    offlineOrdersGoods.setOrdersNo(orderNo);
                } else {
                    offlineOrdersGoods.setOrdersNo("无");
                }
                offlineOrdersGoods.setSize(size[i]);
                if (null != goodsStock) {
                    offlineOrdersGoods.setSku(goodsStock.getSku());
                }
                offlineOrdersGoodsList.add(offlineOrdersGoods);
            }
        } else {
            for (int i = 0; i < div_brand.length; i++) {
                PhOfflineOrdersGoods offlineOrdersGoods = new PhOfflineOrdersGoods();
                PhBrand brand = brandService.findOne(Long.valueOf(div_brand[i]));
                offlineOrdersGoods.setBrandId(brand.getId());
                offlineOrdersGoods.setBrandLogo(brand.getLogo());
                offlineOrdersGoods.setBrandName(brand.getName());
                offlineOrdersGoods.setColor(div_goodsColor[i]);
                offlineOrdersGoods.setWay(div_goodsWay[i]);
                if (StringUtils.isNotBlank(div_goodsExpress[i])) {
                    offlineOrdersGoods.setExpressOrderNo(div_goodsExpress[i]);
                } else {
                    offlineOrdersGoods.setExpressOrderNo("无");
                }
                offlineOrdersGoods.setCreateTime(new Date());
                offlineOrdersGoods.setGoodsId(null);
                offlineOrdersGoods.setGoodsImage(div_goodsImg[i]);
                offlineOrdersGoods.setGoodsName(div_goodsName[i]);
                if (StringUtils.isNotBlank(orderNo)) {
                    offlineOrdersGoods.setOrdersNo(orderNo);
                } else {
                    offlineOrdersGoods.setOrdersNo("无");
                }
                offlineOrdersGoods.setSize(div_goodsSize[i]);
                offlineOrdersGoods.setSku(null);
                offlineOrdersGoodsList.add(offlineOrdersGoods);
            }
        }
        offlineOrdersGoodsService.save(offlineOrdersGoodsList);
        return true;
    }

    @ResponseBody
    @RequestMapping("/offlineOrdersGoods-data")
    public Map<String, Object> offlineOrdersGoodsDate(HttpServletRequest request, String ordersNo) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));

        Page<PhOfflineOrdersGoods> pages = offlineOrdersGoodsService.findByPage(ordersNo, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/offline-edit")
    public Map<String, Object> edit(Long id) {
        Map<String, Object> map = new HashMap<>();
        PhOfflineOrders offlineOrders = offlineOrdersService.findOne(id);
        List<PhOfflineOrdersGoods> list = offlineOrdersGoodsService.findByordersNo(offlineOrders.getOrdersNo());
        map.put("offlineOrders", offlineOrders);
        map.put("offlineOrdersGoods", list);
        return map;
    }

    @ResponseBody
    @RequestMapping("/offline-remarkbyid")
    public Map<String, Object> remarkbyid(HttpServletRequest request, String id) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));

        Page<PhOfflineRemark> pages = offlineRemarkService.findAllByPage(id, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName));
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/offline-addremark")
    public boolean addremark(String id, String content) {
        try {
            PhOfflineRemark offlineRemark = new PhOfflineRemark();
            PhOfflineOrders offlineOrders = offlineOrdersService.findOne(Long.valueOf(id));
            offlineRemark.setContent(content);
            offlineRemark.setCreateTime(new Date());
            offlineRemark.setOrdersNo(offlineOrders.getOrdersNo());
            offlineRemark.setOrdersId(id);
            offlineRemarkService.save(offlineRemark);
            offlineOrders.setMessage("1");
            offlineOrdersService.save(offlineOrders);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("order-addremark异常id:{},content:{}", id, content, e);
            return false;
        }
    }

    @ResponseBody
    @RequestMapping("/offline-delremark")
    public boolean delremark(Long id) {
        offlineRemarkService.delete(id);
        return true;
    }

    @ResponseBody
    @RequestMapping("/offline-deloder")
    public boolean deloder(Long id) {
        offlineOrdersService.delete(id);
        return true;
    }
}
