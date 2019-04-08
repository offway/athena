package cn.offway.athena.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.offway.athena.domain.PhAdmin;
import cn.offway.athena.domain.PhGoods;
import cn.offway.athena.domain.PhGoodsImage;
import cn.offway.athena.properties.QiniuProperties;
import cn.offway.athena.service.PhBrandService;
import cn.offway.athena.service.PhGoodsImageService;
import cn.offway.athena.service.PhGoodsService;


/**
 * 商品管理
 * @author wn
 *
 */
@Controller
public class GoodsController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private PhGoodsService phGoodsService;
	
	@Autowired
	private PhGoodsImageService phGoodsImageService;
	
	@Autowired
	private PhBrandService phBrandService;
	

	/**
	 * 商品
	 * @param map
	 * @return
	 */
	@RequestMapping("/goods.html")
	public String goods(ModelMap map,Authentication authentication){
		map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));

		return "goods";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/goods-data")
	public Map<String, Object> goodsData(HttpServletRequest request,String name,Long brandId,String isOffway,Authentication authentication){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		Page<PhGoods> pages = phGoodsService.findByPage(name.trim(),brandId,isOffway,brandIds, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@PostMapping("/goods-save")
	public boolean save(PhGoods phGoods,String banner,String detail,Authentication authentication){
		try {
			
			String isOffway = "0";
			PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
			List<Long> roleIds = phAdmin.getRoleIds();
			for (Object roleId : roleIds) {
				if("8".equals(String.valueOf(roleId))){
					isOffway = "1"; 
				}
			}
			
			phGoods.setIsOffway(isOffway);
			phGoodsService.save(phGoods, banner, detail);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存商品异常,{}",JSON.toJSONString(phGoods),e);
			return false;
		}
		
	}
	
	@ResponseBody
	@PostMapping("/goods-one")
	public PhGoods findOne(Long id){
		return phGoodsService.findOne(id);
	}
	
	@PostMapping("/goods-update")
	@ResponseBody
	public boolean goodsUpdate(@RequestParam("ids[]") Long[] ids,String status){
		List<PhGoods> phGoodss = phGoodsService.findAll(Arrays.asList(ids));
		for (PhGoods phGoods : phGoodss) {
			if(StringUtils.isNotBlank(status)){
				phGoods.setStatus(status);
			}
		}
		
		phGoodsService.save(phGoodss);
		return true;
	}
	
	@GetMapping("/goods-images")
	@ResponseBody
	public List<PhGoodsImage> images(Long id){
		return phGoodsImageService.findByGoodsId(id);
	}
	
	@PostMapping("/goods-images-delete")
	@ResponseBody
	public boolean imagesDelete(Long goodsImageId){
		return phGoodsService.imagesDelete(goodsImageId);
		
	}
	
	@GetMapping("/goods-brandId")
	@ResponseBody
	public List<PhGoods> goodsBybrandId(Long brandId){
		return phGoodsService.findByBrandId(brandId);
	}
	
	@PostMapping("/goods-delete")
	@ResponseBody
	public String goodsDelete(@RequestParam("ids[]") Long[] ids){
		return phGoodsService.delete(Arrays.asList(ids));
	}
	
	
}
