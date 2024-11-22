package com.lujiachao.login.controller;

import cn.hutool.json.JSONUtil;
import com.lujiachao.login.controller.vo.ResourceResponse;
import com.lujiachao.login.controller.vo.ResourceRequest;
import com.lujiachao.login.entity.Resource;
import com.lujiachao.login.service.ResourceService;
import com.lujiachao.login.utils.BeanCopyUtil;
import com.lujiachao.login.utils.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

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
@Slf4j
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping
    public Integer saveResource(ResourceRequest resourceRequest) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceRequest, resource);
        return resourceService.save(resource) ? 1 : 0;
    }


    @GetMapping("{id}")
    public ResourceResponse findResourceById(@PathVariable("id") @Min(value = 1, message = "id不能小于1") Integer id) {
        Resource resource = Optional.ofNullable(resourceService.getById(id)).orElseThrow(() -> new BusinessException("资源信息不存在"));
        log.info(JSONUtil.toJsonStr(resource));
        return BeanCopyUtil.copy(resource, ResourceResponse.class);
    }

    @GetMapping
    public List<ResourceResponse> findAllResources() {
        List<Resource> list = resourceService.list();
        return BeanCopyUtil.copyList(list, ResourceResponse.class);
    }

    @DeleteMapping("{id}")
    public Boolean deleteResourceById(@PathVariable @Min(value = 1, message = "id不能小于1") Integer id) {
        return resourceService.removeById(id);
    }
}
