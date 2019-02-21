package cn.offway.athena.controller;

import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.offway.athena.domain.PhGoods;
import cn.offway.athena.domain.PhGoodsStock;
import cn.offway.athena.service.PhBrandService;
import cn.offway.athena.service.PhGoodsService;
import cn.offway.athena.service.PhGoodsStockService;


/**
 * 库存管理
 * @author wn
 *
 */
@Controller
public class StockController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhGoodsStockService phGoodsStockService;
	
	@Autowired
	private PhBrandService phBrandService;
	
	@Autowired
	private PhGoodsService phGoodsService;
	

	/**
	 * 库存
	 * @param map
	 * @return
	 */
	@RequestMapping("/stock.html")
	public String stock(ModelMap map){
		map.addAttribute("brands", phBrandService.findAll());
		return "stock";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stock-data")
	public Map<String, Object> stockData(HttpServletRequest request,Long brandId,String brandName,Long goodsId,String goodsName,String isOffway,String color,String size){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhGoodsStock> pages = phGoodsStockService.findByPage(brandId,null!=brandName?brandName.trim():brandName, goodsId, null!=goodsName?goodsName.trim():goodsName, isOffway.trim(), color.trim(), size.trim(), new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@PostMapping("/stock-save")
	public boolean save(PhGoodsStock phGoodsStock){
		try {
			
			Long goodsId = phGoodsStock.getGoodsId();

			boolean check = true;
			if(null!=phGoodsStock.getId()){
				
				PhGoodsStock goodsStock = phGoodsStockService.findOne(phGoodsStock.getId());
				if(goodsStock.getColor().equals(phGoodsStock.getColor()) && goodsStock.getSize().equals(phGoodsStock.getSize())){
					check = false;
				}
			}
			
			if(check){
				int count = phGoodsStockService.countByGoodsIdAndColorAndSize(goodsId, phGoodsStock.getColor(), phGoodsStock.getSize());
				if(count>0){
					return false;
				}
			}
			
			PhGoods phGoods = phGoodsService.findOne(goodsId);
			phGoodsStock.setGoodsName(phGoods.getName());
			phGoodsStock.setImage(phGoods.getImage());
			phGoodsStock.setBrandId(phGoods.getBrandId());
			phGoodsStock.setBrandLogo(phGoods.getBrandLogo());
			phGoodsStock.setBrandName(phGoods.getBrandName());
			phGoodsStock.setIsOffway(phGoods.getIsOffway());
			phGoodsStock.setCategory(phGoods.getCategory());
			phGoodsStock.setType(phGoods.getType());
			phGoodsStock.setCreateTime(new Date());
			phGoodsStockService.save(phGoodsStock);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存库存异常,{}",JSON.toJSONString(phGoodsStock),e);
			return false;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/stock-one")
	public PhGoodsStock findOne(Long id){
		return phGoodsStockService.findOne(id);
	}
	
}
