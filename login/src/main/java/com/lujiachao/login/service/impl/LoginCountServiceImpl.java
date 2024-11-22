package com.lujiachao.login.service.impl;

import com.lujiachao.login.entity.LoginCount;
import com.lujiachao.login.mapper.LoginCountMapper;
import com.lujiachao.login.service.ILoginCountService;
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
public class LoginCountServiceImpl extends ServiceImpl<LoginCountMapper, LoginCount> implements ILoginCountService {

}
