package com.lujiachao.login.service.impl;

import com.lujiachao.login.entity.User;
import com.lujiachao.login.mapper.UserMapper;
import com.lujiachao.login.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
