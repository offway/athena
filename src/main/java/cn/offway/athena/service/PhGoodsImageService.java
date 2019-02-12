package cn.offway.athena.service;

import cn.offway.athena.domain.PhGoodsImage;

/**
 * 商品图片Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsImageService{

	PhGoodsImage save(PhGoodsImage phGoodsImage);
	
	PhGoodsImage findOne(Long id);
}
