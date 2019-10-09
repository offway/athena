package cn.offway.athena.controller;

import cn.offway.athena.domain.*;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 订单查询
 * @author wn
 *
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
	
	@RequestMapping("/offline.html")
	public String order(ModelMap map){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("theId", "XYZ");
		return "offline";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/offline-data")
	public Map<String, Object> offlineData(HttpServletRequest request,String realName,String users,String state,String ordersNo){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		Page<PhOfflineOrders> pages = offlineOrdersService.findByPage(realName,users,state,ordersNo,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	public boolean confirm(Long id,String state){
		PhOfflineOrders offlineOrders = offlineOrdersService.findOne(id);
		/*0-确认发货,1-确认收货*/
		/*0-未寄出,1-已寄出,2-已寄出/未收回,3-已寄出/已收回*/
		if ("0".equals(state)){
			offlineOrders.setState("1");
		}else {
			offlineOrders.setState("3");
		}
		offlineOrdersService.save(offlineOrders);
		return true;
	}

	@ResponseBody
	@RequestMapping("/offline-save")
	public boolean save(PhOfflineOrders offlineOrders,@RequestParam("goodsID")String[] goodsID,@RequestParam("size") String[] size,@RequestParam("color") String[] color,@RequestParam("expressOrderNo")String[] expressOrderNo,@RequestParam("way") String[] way){
		if (size.length != color.length && color.length != goodsID.length){
			return false;
		}
		String orderNo = "";
		List<PhOfflineOrdersGoods> offlineOrdersGoodsList = new ArrayList<>();
		if ("".equals(offlineOrders.getOrdersNo())) {
			orderNo = orderInfoService.generateOrderNo("PH") + "XX";
			offlineOrders.setCreateTime(new Date());
			offlineOrders.setState("0");
			offlineOrders.setOrdersNo(orderNo);
			offlineOrders.setGoodsCount((long) goodsID.length);
			offlineOrdersService.save(offlineOrders);
		} else {
			orderNo = offlineOrders.getOrdersNo();
			offlineOrders.setGoodsCount((long) goodsID.length);
			offlineOrders.setState("0");
			offlineOrdersService.save(offlineOrders);
			offlineOrdersGoodsService.delbyOrdersNo(orderNo);
		}
		for (int i=0;i<goodsID.length;i++){
			PhGoods goods = goodsService.findOne(Long.valueOf(goodsID[i]));
			PhGoodsStock goodsStock = goodsStockService.findOne(Long.valueOf(goodsID[i]));
			PhOfflineOrdersGoods offlineOrdersGoods = new PhOfflineOrdersGoods();
			offlineOrdersGoods.setBrandId(goods.getBrandId());
			offlineOrdersGoods.setBrandLogo(goods.getBrandLogo());
			offlineOrdersGoods.setBrandName(goods.getBrandName());
			offlineOrdersGoods.setColor(color[i]);
			if ("" != way[i]){
				offlineOrdersGoods.setWay(way[i]);
			}
			if ("" != expressOrderNo[i]){
				offlineOrdersGoods.setExpressOrderNo(expressOrderNo[i]);
			}else {
				offlineOrdersGoods.setExpressOrderNo("无");
			}
			offlineOrdersGoods.setCreateTime(new Date());
			offlineOrdersGoods.setGoodsId(goods.getId());
			offlineOrdersGoods.setGoodsImage(goods.getImage());
			offlineOrdersGoods.setGoodsName(goods.getName());
			if (""!= orderNo){
				offlineOrdersGoods.setOrdersNo(orderNo);
			}else {
				offlineOrdersGoods.setOrdersNo("无");
			}
			offlineOrdersGoods.setSize(size[i]);
			if (null != goodsStock){
				offlineOrdersGoods.setSku(goodsStock.getSku());
			}
			offlineOrdersGoodsList.add(offlineOrdersGoods);
		}
		offlineOrdersGoodsService.save(offlineOrdersGoodsList);

		return true;
	}

	@ResponseBody
	@RequestMapping("/offlineOrdersGoods-data")
	public Map<String,Object> offlineOrdersGoodsDate(HttpServletRequest request,String ordersNo){
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));

		Page<PhOfflineOrdersGoods> pages = offlineOrdersGoodsService.findByPage(ordersNo,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	public Map<String,Object> edit(Long id){
		Map<String,Object> map = new HashMap<>();
		PhOfflineOrders offlineOrders = offlineOrdersService.findOne(id);
		List<PhOfflineOrdersGoods> list = offlineOrdersGoodsService.findByordersNo(offlineOrders.getOrdersNo());
		map.put("offlineOrders",offlineOrders);
		map.put("offlineOrdersGoods",list);
		return map;
	}

}
