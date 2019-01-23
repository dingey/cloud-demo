package com.d.store.impl;

import com.d.store.service.StoreService;
import com.d.base.BaseServiceImpl;
import com.d.store.mapper.StoreMapper;
import com.d.store.entity.Store;
import org.springframework.stereotype.Service;
/**
 * 店铺service
 * @author d
 * @date 2019-01-16 21:06
 */
@Service("storeService")
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper,Store> implements StoreService {

}