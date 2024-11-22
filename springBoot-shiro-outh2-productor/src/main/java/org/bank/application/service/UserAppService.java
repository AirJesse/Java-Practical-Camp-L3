package org.bank.application.service;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bank.common.constant.Constant;
import org.bank.common.constant.ReqType;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.shiro.CustomPasswordToken;
import org.bank.common.util.JwtTokenUtil;
import org.bank.common.util.PageUtils;
import org.bank.common.util.PasswordUtils;
import org.bank.common.util.TokenSettings;
import org.bank.domain.entity.SysDept;
import org.bank.domain.entity.SysRole;
import org.bank.domain.entity.SysUser;
import org.bank.domain.repository.SysDeptRepository;
import org.bank.domain.repository.SysUserRepository;
import org.bank.domain.service.*;
import org.bank.domain.vo.req.*;
import org.bank.domain.vo.resp.LoginRespVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.UserInfoForVueRespVO;
import org.bank.domain.vo.resp.UserOwnRoleRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserAppService implements UserService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TokenSettings tokenSettings;
    @Autowired
    private SysDeptRepository sysDeptRepository;

    public static void main(String[] args) {
        String wangxiaoming666 = PasswordUtils.encode("wangxiaoming666", "1d69ee8edf304fee8d27");
        System.out.println(wangxiaoming666);
    }

    @Override
    public String register(RegisterReqVO vo) {
        SysUser sysUser = new SysUser();
        //校验账号是否存在
        String username = vo.getUsername();
        sysUser = sysUserRepository.getUserInfoByName(username);
        if (StringUtils.isEmpty(sysUser.getId())) {
            sysUser = new SysUser();
        } else {
            throw new BusinessException(BaseResponseCode.ACCOUNT_ISEXIT);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        int i = sysUserRepository.insertSelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        log.info("UserService方法【LoginRespVO】传入参数" + vo.toString());
        SysUser sysUser = sysUserRepository.getUserInfoByName(vo.getUsername());
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (sysUser.getStatus() == 2) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        LoginRespVO respVO = new LoginRespVO();
        BeanUtils.copyProperties(sysUser, respVO);
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(sysUser.getId()));
        claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(sysUser.getId()));
        claims.put(Constant.JWT_USER_NAME, sysUser.getUsername());

        String access_token = JwtTokenUtil.getAccessToken(sysUser.getId(), claims);
        String refresh_token;
        if (vo.getType().equals("1")) {
            refresh_token = JwtTokenUtil.getRefreshToken(sysUser.getId(), claims);
        } else {
            refresh_token = JwtTokenUtil.getRefreshAppToken(sysUser.getId(), claims);
        }
        SecurityUtils.getSubject().login(new CustomPasswordToken(access_token, ReqType.SYSTEM.toString()));
        respVO.setAccessToken(access_token);
        respVO.setRefreshToken(refresh_token);
        return respVO;
    }

    @Override
    public String loginForOuth(LoginReqVO vo) {
        SysUser sysUser = sysUserRepository.getUserInfoByName(vo.getUsername());
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (sysUser.getStatus() == 2) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }
        return sysUser.getId();
    }

    private List<String> getRolesByUserId(String userId) {
        return roleService.getRoleNames(userId);
    }

    private Set<String> getPermissionsByUserId(String userId) {
        return permissionService.getPermissionsByUserId(userId);
    }

    @Override
    public String refreshToken(String refreshToken, String accessToken) {

        if (redisService.hasKey(Constant.JWT_ACCESS_TOKEN_BLACKLIST + refreshToken) || !JwtTokenUtil.validateToken(refreshToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        String userId = JwtTokenUtil.getUserId(refreshToken);
        log.info("userId={}", userId);
        SysUser sysUser = sysUserRepository.selectByPrimaryKey(userId);
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
        Map<String, Object> claims = null;

        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            claims = new HashMap<>();
            claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(userId));
            claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(userId));
        }
        String newAccessToken = JwtTokenUtil.refreshToken(refreshToken, claims);

        redisService.setifAbsen(Constant.JWT_REFRESH_STATUS + accessToken, userId, 1, TimeUnit.MINUTES);


        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION + newAccessToken, userId, redisService.getExpire(Constant.JWT_REFRESH_KEY + userId, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        }
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo, String operationId) {

        SysUser sysUser = sysUserRepository.selectByPrimaryKey(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(vo.getPassword())) {
            String newPassword = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
            sysUser.setPassword(newPassword);
        } else {
            sysUser.setPassword(null);
        }
        sysUser.setUpdateId(operationId);
        int count = sysUserRepository.updateByPrimaryKeySelective(sysUser);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (vo.getRoleIds() != null && vo.getRoleIds().size() > 0) {
            setUserOwnRole(sysUser.getId(), vo.getRoleIds());
        }
        if (vo.getStatus() != null && vo.getStatus() == 2) {
            redisService.set(Constant.ACCOUNT_LOCK_KEY + sysUser.getId(), sysUser.getId());
        } else {
            redisService.delete(Constant.ACCOUNT_LOCK_KEY + sysUser.getId());
        }

    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysUser> sysUsers = sysUserRepository.selectAll(vo);
        if (!sysUsers.isEmpty()) {
            for (SysUser sysUser : sysUsers) {
                SysDept sysDept = sysDeptRepository.selectByPrimaryKey(sysUser.getDeptId());
                if (sysDept != null) {
                    sysUser.setDeptName(sysDept.getName());
                }
                //获取其roles  vue页面使用
                List<SysRole> roles = roleService.getRoleInfoByUserId(sysUser.getId());
                sysUser.setRoles(roles);
            }
        }

        return PageUtils.getPageVO(sysUsers);
    }

    @Override
    public SysUser detailInfo(String userId) {

        return sysUserRepository.selectByPrimaryKey(userId);
    }

    @Override
    public UserInfoForVueRespVO detailInfoForVue(String userId) {
        SysUser user = detailInfo(userId);
        if (StringUtils.isEmpty(user.getId())) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        SysDept sysDept = sysDeptRepository.selectByPrimaryKey(user.getDeptId());
        if (sysDept != null) {
            user.setDeptName(sysDept.getName());
        }
        UserInfoForVueRespVO respVO = new UserInfoForVueRespVO();
        respVO.setUser(user);
        //查询权限
        Set<String> permissionList = permissionService.getPermissionsByUserId(userId);
        if (permissionList != null) {
            respVO.setRoles(new ArrayList(permissionList));
        } else {
            respVO.setRoles(new ArrayList());
        }
        return respVO;
    }

    @Override
    public PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserRepository.selectUserInfoByDeptIds(deptIds);
        return PageUtils.getPageVO(list);
    }

    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser = null;
        if (StringUtils.isEmpty(vo.getPassword())) {
            vo.setPassword("123456");
        }
        //校验账号是否存在
        String username = vo.getUsername();
        sysUser = sysUserRepository.getUserInfoByName(username);
        if (sysUser == null) {
            sysUser = new SysUser();
        } else {
            throw new BusinessException(BaseResponseCode.ACCOUNT_ISEXIT);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());


        int i = sysUserRepository.insertSelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (null != vo.getRoleIds() && !vo.getRoleIds().isEmpty()) {
            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(sysUser.getId());
            reqVO.setRoleIds(vo.getRoleIds());
            userRoleService.addUserRoleInfo(reqVO);
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}", subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId = JwtTokenUtil.getUserId(accessToken);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);

        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken) {

        SysUser sysUser = sysUserRepository.selectByPrimaryKey(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(), vo.getOldPwd(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordUtils.encode(vo.getNewPwd(), sysUser.getSalt()));
        int i = sysUserRepository.updateByPrimaryKeySelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);

        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);


        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);

    }

    @Override
    public List<SysUser> getUserListByDeptId(String deptId) {
        return sysUserRepository.getUserListByDeptId(deptId);
    }

    @Override
    public List<SysUser> getUserListByDeptIds(List<String> deptIds) {
        return sysUserRepository.selectUserInfoByDeptIds(deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedUsers(List<String> userIds, String operationId) {
        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        sysUser.setDeleted(0);
        int i = sysUserRepository.deletedUsers(sysUser, userIds);
        if (i == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        for (String userId : userIds) {
            //清空权鉴缓存
            redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
        }

    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        List<SysRole> list = roleService.selectAllRoles();
        UserOwnRoleRespVO vo = new UserOwnRoleRespVO();
        vo.setAllRole(list);
        vo.setOwnRoles(roleIdsByUserId);
        return vo;
    }

    @Override
    public void setUserOwnRole(String userId, List<String> roleIds) {

        UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
        reqVO.setUserId(userId);
        reqVO.setRoleIds(roleIds);
        userRoleService.addUserRoleInfo(reqVO);
        redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
        //清空权鉴缓存
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

}
