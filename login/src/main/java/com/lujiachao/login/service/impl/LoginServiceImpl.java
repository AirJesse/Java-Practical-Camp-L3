package com.lujiachao.login.service.impl;

import com.lujiachao.login.entity.Login;
import com.lujiachao.login.mapper.LoginMapper;
import com.lujiachao.login.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户token信息表 服务实现类
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

}
