package org.bank.application.api;

import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.service.UserRoleService;
import org.bank.domain.vo.req.UserRoleOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.controller
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@RequestMapping("/sys")
@RestController
public class UserRoleApi {
    @Autowired
    private UserRoleService userRoleService;
    
    @PostMapping("/user/role")
    @LogAnnotation(title = "用户和角色关联接口",action = "修改或者新增用户角色")
    public DataResult operationUserRole(@RequestBody @Valid UserRoleOperationReqVO vo){
        userRoleService.addUserRoleInfo(vo);
        return DataResult.success();
    }
}
