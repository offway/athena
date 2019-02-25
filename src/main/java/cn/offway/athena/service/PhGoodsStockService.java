package cn.offway.athena.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.offway.athena.domain.PhGoodsStock;

/**
 * 商品库存Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsStockService{

	PhGoodsStock save(PhGoodsStock phGoodsStock);
	
	PhGoodsStock findOne(Long id);

	int countByGoodsIdAndColorAndSize(Long goodsId, String color, String size);

	Page<PhGoodsStock> findByPage(Long brandId, String brandName, Long goodsId, String goodsName, String isOffway,
			String color, String size, Pageable page);

	int updateStock(String orderNo);
}
