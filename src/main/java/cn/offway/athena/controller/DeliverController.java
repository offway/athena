package cn.offway.athena.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.offway.athena.domain.PhOrderExpressInfo;
import cn.offway.athena.domain.PhOrderGoods;
import cn.offway.athena.domain.VOrder;
import cn.offway.athena.service.PhOrderExpressInfoService;
import cn.offway.athena.service.PhOrderGoodsService;
import cn.offway.athena.service.PhOrderInfoService;
import cn.offway.athena.service.VOrderService;
import cn.offway.athena.utils.JsonResult;

/**
 * 发货
 * @author wn
 *
 */
@Controller
public class DeliverController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VOrderService vOrderService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@RequestMapping("/deliver.html")
	public String deliver(ModelMap map){
		return "deliver";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param deliver
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deliver-data")
	public Map<String, Object> deliverData(HttpServletRequest request,String orderNo, String unionid){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<VOrder> pages = vOrderService.findByPage(orderNo.trim(),unionid.trim(), new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@RequestMapping("/deliver-one")
	public VOrder findByOrderNo(String orderNo){
		return vOrderService.findByOrderNo(orderNo);
	}
	
	@ResponseBody
	@RequestMapping("/deliver-updateAddr")
	public boolean updateAddr(String id,String toProvince,String toCity,String toCounty,String toContent){
		PhOrderExpressInfo phOrderExpressInfo =  phOrderExpressInfoService.findByOrderNoAndType(id, "0");
//		phOrderExpressInfo.setToCity(toCity);
		phOrderExpressInfo.setToContent(toContent);
//		phOrderExpressInfo.setToCounty(toCounty);
//		phOrderExpressInfo.setToProvince(toProvince);
		phOrderExpressInfoService.save(phOrderExpressInfo);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/deliver-goods")
	public List<PhOrderGoods> phOrderGoods(String orderNo){
		return phOrderGoodsService.findByOrderNo(orderNo);
	}
	
	@ResponseBody
	@RequestMapping("/deliver-save")
	public JsonResult save(String orderNo){
		return phOrderInfoService.saveOrder(orderNo);
	}
	
	
	
}
