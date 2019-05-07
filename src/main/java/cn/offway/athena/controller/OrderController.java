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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.offway.athena.domain.PhAdmin;
import cn.offway.athena.domain.PhOrderExpressInfo;
import cn.offway.athena.domain.PhOrderGoods;
import cn.offway.athena.domain.PhOrderInfo;
import cn.offway.athena.domain.VOrder;
import cn.offway.athena.service.PhBrandService;
import cn.offway.athena.service.PhGoodsStockService;
import cn.offway.athena.service.PhOrderExpressInfoService;
import cn.offway.athena.service.PhOrderGoodsService;
import cn.offway.athena.service.PhOrderInfoService;
import cn.offway.athena.utils.JsonResult;

/**
 * 订单查询
 * @author wn
 *
 */
@Controller
public class OrderController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Autowired
	private PhBrandService phBrandService;
	
	
	@RequestMapping("/order.html")
	public String order(ModelMap map,Authentication authentication){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		return "order";
	}
	
	@RequestMapping("/order-return.html")
	public String orderReturn(ModelMap map,Authentication authentication){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		return "order-return";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/order-data")
	public Map<String, Object> orderData(HttpServletRequest request,String orderNo, String unionid,
			String realName, String position,String status,Long brandId,String isOffway,String isUpload,Authentication authentication){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		Page<PhOrderInfo> pages = phOrderInfoService.findByPage(isUpload,realName.trim(),position.trim(), orderNo.trim(),null!=unionid?unionid.trim():unionid,status.trim(),brandId,isOffway,brandIds, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@RequestMapping("/order-goods")
	public List<PhOrderGoods> phOrderGoods(String orderNo){
		return phOrderGoodsService.findByOrderNo(orderNo);
	}
	
	@ResponseBody
	@RequestMapping("/order-express")
	public PhOrderExpressInfo phOrderExpress(String orderNo,String type){
		return phOrderExpressInfoService.findByOrderNoAndType(orderNo, type);
	}
	
	@ResponseBody
	@RequestMapping("/order-check")
	public boolean orderCheck(String orderNo){
		try {
			return phGoodsStockService.updateStock(orderNo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("order-check异常orderNo:{}",orderNo,e);
			return false;
		}
		
	}
	
	
	
	
	
}
