package com.d.goods.impl;

import com.d.base.BaseServiceImpl;
import com.d.goods.entity.InventoryRecord;
import com.d.goods.mapper.InventoryRecordMapper;
import com.d.goods.service.InventoryRecordService;
import org.springframework.stereotype.Service;

/**
 * @auther d
 */
@Service
public class InventoryRecordServiceImpl extends BaseServiceImpl<InventoryRecordMapper,InventoryRecord> implements InventoryRecordService{
}
