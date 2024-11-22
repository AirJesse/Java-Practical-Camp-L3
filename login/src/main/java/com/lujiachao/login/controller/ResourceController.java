package com.lujiachao.login.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import com.lujiachao.login.controller.dto.ResourceRequest;
import com.lujiachao.login.entity.Resource;
import com.lujiachao.login.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 资源表 前端控制器
 * 对应教程中 DataController
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping
    public boolean saveResource(ResourceRequest resourceRequest) {
        System.out.println("hello");
//        Resource resource = new Resource();
//        BeanUtils.copyProperties(resourceRequest, resource);
//        return resourceService.save(resource);
        return true;
    }
}
