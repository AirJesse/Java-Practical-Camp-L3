package com.lujiachao.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lujiachao.login.entity.Login;
import com.lujiachao.login.entity.LoginCount;
import com.lujiachao.login.mapper.LoginCountMapper;
import com.lujiachao.login.service.LoginCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录信息统计表 服务实现类
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@Service
public class LoginCountServiceImpl extends ServiceImpl<LoginCountMapper, LoginCount> implements LoginCountService {

    @Override
    public int addLoginCount(String userName) {
        LoginCount one = getOne(new LambdaQueryWrapper<LoginCount>().eq(LoginCount::getUserName, userName));
        if (one == null) {
            LoginCount loginCount = new LoginCount();
            loginCount.setUserName(userName);
            loginCount.setCount(1);
            save(loginCount);
            return 1;
        }
        one.setCount(one.getCount() + 1);
        updateById(one);
        return one.getCount();
    }
}
