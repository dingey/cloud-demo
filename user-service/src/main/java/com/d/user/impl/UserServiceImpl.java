package com.d.user.impl;

import com.d.base.BaseServiceImpl;
import com.d.user.entity.User;
import com.d.user.mapper.UserMapper;
import com.d.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper,User> implements UserService {
}