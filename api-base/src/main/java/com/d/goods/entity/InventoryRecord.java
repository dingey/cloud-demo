package com.d.goods.entity;

import com.d.base.BaseEntity;
import lombok.Data;

/**
 * 库存记录
 *
 * @auther d
 * @date 2019/2/14 16:43
 * @since 0.0.1
 */
@Data
public class InventoryRecord extends BaseEntity<InventoryRecord> {
    private Long skuId;
    private Long orderId;
    private Integer qty;
}
