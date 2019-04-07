package cn.offway.athena.controller;

import java.util.Date;
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
import cn.offway.athena.domain.PhShowImage;
import cn.offway.athena.service.PhBrandService;
import cn.offway.athena.service.PhShowImageService;

/**
 * 返图审核
 * @author wn
 *
 */
@Controller
public class ShowImageController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhShowImageService phShowImageService;
	
	@Autowired
	private PhBrandService phBrandService;
	
	@RequestMapping("/showImage.html")
	public String showImage(ModelMap map,Authentication authentication){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		return "showImage";
	}
	
	@RequestMapping("/showImage-info.html")
	public String showImageInfo(ModelMap map,Authentication authentication){
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		
		map.addAttribute("brands", phBrandService.findByIds(brandIds));
		return "showImage-info";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param showImage
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/showImage-data")
	public Map<String, Object> showImageData(HttpServletRequest request,String orderNo, String unionid,String status,Long brandId,String isOffway,Authentication authentication){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		
		PhAdmin phAdmin = (PhAdmin)authentication.getPrincipal();
		List<Long> brandIds = phAdmin.getBrandIds();
		Page<PhShowImage> pages = phShowImageService.findByPage(orderNo.trim(),unionid.trim(),status.trim(),brandId,isOffway, brandIds,new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
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
	@RequestMapping("/showImage-check")
	public boolean showImageCheck(Long id,String status,String checkContent,Authentication authentication){
		PhShowImage phShowImage = phShowImageService.findOne(id);
		phShowImage.setStatus(status);
		phShowImage.setCheckContent(checkContent);
		phShowImage.setCheckName(authentication.getName());
		phShowImage.setCheckTime(new Date());
		phShowImageService.save(phShowImage);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/showImage-info")
	public PhShowImage showImageInfo(Long id){
		PhShowImage phShowImage = phShowImageService.findOne(id);
		return phShowImage;
	}
	
	
	
	
	
}
