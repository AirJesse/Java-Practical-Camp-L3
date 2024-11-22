package org.bank.domain.service;


import org.bank.domain.entity.SysUser;
import org.bank.domain.vo.req.*;
import org.bank.domain.vo.resp.LoginRespVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.UserInfoForVueRespVO;
import org.bank.domain.vo.resp.UserOwnRoleRespVO;

import java.util.List;


public interface UserService {

    String register(RegisterReqVO vo);

    LoginRespVO login(LoginReqVO vo);


    String loginForOuth(LoginReqVO vo) ;


    String refreshToken(String refreshToken,String accessToken);

    void updateUserInfo(UserUpdateReqVO vo, String operationId);


    PageVO<SysUser> pageInfo(UserPageReqVO vo);

    SysUser detailInfo(String userId);

    UserInfoForVueRespVO detailInfoForVue(String userId);



    PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds);

    void addUser(UserAddReqVO vo);

    void logout(String accessToken,String refreshToken);

    void updatePwd(UpdatePasswordReqVO vo,String userId,String accessToken, String refreshToken);

    List<SysUser> getUserListByDeptId(String deptId);

    List<SysUser> getUserListByDeptIds(List<String> deptIds);

    void deletedUsers(List<String> userIds,String operationId);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void setUserOwnRole(String userId,List<String> roleIds);
}
