package cn.offway.athena.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.athena.domain.PhGoodsStock;

/**
 * 商品库存Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface PhGoodsStockRepository extends JpaRepository<PhGoodsStock,Long>,JpaSpecificationExecutor<PhGoodsStock> {

	int countByGoodsIdAndColorAndSize(Long goodsId,String color,String size);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="update ph_goods_stock s set s.stock =  s.stock+1 where EXISTS(SELECT 1 from ph_order_goods g where g.order_no =?1 and g.goods_id = s.goods_id and g.color = s.color and g.size= s.size)")
	int updateStock(String orderNo);
}
