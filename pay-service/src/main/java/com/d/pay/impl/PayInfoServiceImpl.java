package com.d.pay.impl;

import com.d.pay.service.PayInfoService;
import com.d.base.BaseServiceImpl;
import com.d.pay.mapper.PayInfoMapper;
import com.d.pay.entity.PayInfo;
import org.springframework.stereotype.Service;
/**
 * 支付单service
 * @author d
 * @date 2019-01-16 21:10
 */
@Service("payInfoService")
public class PayInfoServiceImpl extends BaseServiceImpl<PayInfoMapper,PayInfo> implements PayInfoService {

}