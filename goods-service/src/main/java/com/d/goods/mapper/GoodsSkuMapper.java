package com.d.goods.mapper;

import com.d.base.BaseMapper;
import com.d.goods.entity.GoodsSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品SKUMapper接口
 *
 * @author d
 * @date 2019-01-16 21:02
 */
public interface GoodsSkuMapper extends BaseMapper<GoodsSku> {
    @Update("update goods_sku set qty=qty-#{qty} where id=#{id} and qty>=#{qty}")
    Long reduceInventory(@Param("id") Long id, @Param("qty") Integer qty);

    @Update("update goods_sku set qty=qty+#{qty} where id=#{id}")
    Long increaseInventory(@Param("id") Long id, @Param("qty") Integer qty);
}