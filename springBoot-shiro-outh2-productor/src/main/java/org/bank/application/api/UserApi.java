package org.bank.application.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.constant.Constant;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.DataResult;
import org.bank.common.util.JwtTokenUtil;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.SysUser;
import org.bank.domain.service.UserService;
import org.bank.domain.vo.req.*;
import org.bank.domain.vo.resp.LoginRespVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.UserInfoForVueRespVO;
import org.bank.domain.vo.resp.UserOwnRoleRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.controller
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/sys")
public class UserApi {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/login")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO vo){
        DataResult<LoginRespVO> result= DataResult.success();
        result.setData(userService.login(vo));
        return result;
    }

    @PostMapping("/user/getUserForVUE")
    @LogAnnotation(title = "用户管理",action = "查询用户详情(Vue页面接口)")
    public DataResult<UserInfoForVueRespVO> detailInfoForVue(HttpServletRequest request){
        String userId= JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        DataResult<UserInfoForVueRespVO> result=DataResult.success();
        result.setData(userService.detailInfoForVue(userId));
        return result;
    }

    @PostMapping("/user/register")
    public DataResult<String> register(@RequestBody @Valid RegisterReqVO vo){
        DataResult<String> result=DataResult.success();
        result.setData(userService.register(vo));
        return result;
    }
    @GetMapping("/user/token")
    @LogAnnotation(title = "用户管理",action = "用户刷新token")
    public DataResult<String> refreshToken(HttpServletRequest request){
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        String accessToken=request.getHeader(Constant.ACCESS_TOKEN);
        DataResult<String> result=DataResult.success();
        result.setData(userService.refreshToken(refreshToken,accessToken));
        return result;
    }

    @GetMapping("/user/unLogin")
    public DataResult unLogin(){
        DataResult result= DataResult.getResult(BaseResponseCode.TOKEN_ERROR);
        return result;
    }

    @PutMapping("/user")
    @LogAnnotation(title = "用户管理",action = "更新用户信息")
    @RequiresPermissions("sys:user:update")
    public DataResult updateUserInfo(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request){
        String operationId= JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.updateUserInfo(vo,operationId);
        return DataResult.success();
    }

    @PutMapping("/user/info")
    @LogAnnotation(title = "用户管理",action = "更新用户信息")
    public DataResult updateUserInfoById(@RequestBody @Valid UserUpdateReqVO vo,HttpServletRequest request){
        String operationId= JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        vo.setId(operationId);
        userService.updateUserInfo(vo,operationId);
        return DataResult.success();
    }

    @GetMapping("/user/{id}")
    @LogAnnotation(title = "用户管理",action = "查询用户详情")
    @RequiresPermissions("sys:user:detail")
    public DataResult<SysUser> detailInfo(@PathVariable("id") String id){
        DataResult<SysUser> result=DataResult.success();
        result.setData(userService.detailInfo(id));
        return result;
    }

    @GetMapping("/user")
    @LogAnnotation(title = "用户管理",action = "查询用户详情")
    public DataResult<SysUser> youSelfInfo(HttpServletRequest request){
        String id=JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        DataResult<SysUser> result=DataResult.success();
        result.setData(userService.detailInfo(id));
        return result;
    }

    @PostMapping("/users")
    @RequiresPermissions("sys:user:list")
    @LogAnnotation(title = "用户管理",action = "分页获取用户列表")
    public DataResult<PageVO<SysUser>> pageInfo(@RequestBody UserPageReqVO vo){
        DataResult<PageVO<SysUser>> result=DataResult.success();
        result.setData(userService.pageInfo(vo));
        return result;
    }

    @PostMapping("/user")
    @RequiresPermissions("sys:user:add")
    @LogAnnotation(title = "用户管理",action = "新增用户")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO vo){
        userService.addUser(vo);
        return DataResult.success();
    }

    @GetMapping("/user/logout")
    @LogAnnotation(title = "用户管理",action = "退出")
    public DataResult logout(HttpServletRequest request){
        String accessToken=request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        userService.logout(accessToken,refreshToken);
        return DataResult.success();
    }

    @PutMapping("/user/pwd")
    @LogAnnotation(title = "用户管理",action = "更新密码")
    public DataResult updatePwd(@RequestBody UpdatePasswordReqVO vo,HttpServletRequest request){
        String accessToken=request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
        String userId=JwtTokenUtil.getUserId(accessToken);
        userService.updatePwd(vo,userId,accessToken,refreshToken);
        return DataResult.success();
    }

    @DeleteMapping("/user")
    @LogAnnotation(title = "用户管理",action = "删除用户")
    @RequiresPermissions("sys:user:deleted")
    public DataResult deletedUser(@RequestBody List<String> userIds, HttpServletRequest request){
        String operationId=JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.deletedUsers(userIds,operationId);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @LogAnnotation(title = "用户管理",action = "赋予角色-获取所有角色接口")
    @RequiresPermissions("sys:user:role:detail")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId")String userId){
        DataResult<UserOwnRoleRespVO> result=DataResult.success();
       result.setData(userService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles/{userId}")
    @LogAnnotation(title = "用户管理",action = "赋予角色-用户赋予角色接口")
    @RequiresPermissions("sys:user:role:update")
    public DataResult<UserOwnRoleRespVO> setUserOwnRole(@PathVariable("userId")String userId, @RequestBody List<String> roleIds){
        DataResult result=DataResult.success();
        userService.setUserOwnRole(userId,roleIds);
        return result;
    }
}
