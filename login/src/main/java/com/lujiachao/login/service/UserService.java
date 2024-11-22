package com.lujiachao.login.service;

import cn.dev33.satoken.util.SaResult;
import com.lujiachao.login.controller.vo.LoginRequest;
import com.lujiachao.login.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
public interface UserService extends IService<User> {

    User getUserForLogin(String username, String password);


}
