package com.lujiachao.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Override
    public User getUserForLogin(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username).and(wrapper -> wrapper.eq(User::getPassword, password));
        return this.getOne(queryWrapper);

    }
}
