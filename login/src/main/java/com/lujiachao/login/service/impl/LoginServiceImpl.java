package com.lujiachao.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lujiachao.login.entity.Login;
import com.lujiachao.login.mapper.LoginMapper;
import com.lujiachao.login.service.LoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements LoginService {

    @Override
    public Login updateToken(Login login) {
        Login one = getOne(new LambdaQueryWrapper<Login>().eq(Login::getUserName, login.getUserName()));
        if (one == null) {
            save(login);
        } else {
            login.setId(one.getId());
            updateById(login);
        }
        return login;
    }

}
