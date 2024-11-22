package org.bank.common.constant;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.constants
 * @Author: lizongle
 * @Description: 常量
 * @Version: 1.0
 */
public class Constant {
    /**
     * 正常token
     */
    public static final String ACCESS_TOKEN="authorization";
    /**
     * 刷新token
     */
    public static final String REFRESH_TOKEN="refresh_token";

    /**
     * 创建时间
     */
    public static final String CREATED = "created";

    /**
     * 权限key
     */
    public static final String JWT_PERMISSIONS_KEY="jwt-permissions-key";

    /**
     * 用户名称 key
     */
    public static final String JWT_USER_NAME="jwt-user-name-key";

    /**
     * 角色key
     */
    public static final String JWT_ROLES_KEY="jwt-roles-key_";

    /**
     * 主动去刷新 token key(适用场景 比如修改了用户的角色/权限去刷新token)
     */
    public static final String JWT_REFRESH_KEY="jwt-refresh-key_";

    /**
     *  刷新状态(适用场景如：一个功能点要一次性请求多个接口，当第一个请求刷新接口时候 加入一个节点下一个接口请求进来的时候直接放行)
     */
    public static final String JWT_REFRESH_STATUS="jwt-refresh-status_";

    /**
     * 标记新的access_token
     */
    public static final String JWT_REFRESH_IDENTIFICATION="jwt-refresh-identification_";

    /**
     * access_token 主动退出后加入黑名单 key
     */
    public static final String JWT_ACCESS_TOKEN_BLACKLIST="jwt-access-token-blacklist_";

    /**
     * refresh_token 主动退出后加入黑名单 key
     */
    public static final String JWT_REFRESH_TOKEN_BLACKLIST="jwt-refresh-token-blacklist_";

    /**
     * 组织机构编码key
     */
    public static final String DEPT_CODE_KEY="dept-code-key_";

    /**
     * 菜单权限编码key
     */
    public static final String PERMISSION_CODE_KEY="permission-code-key_";

    /**
     * 标记用户是否已经删除
     */
    public static final String DELETED_USER_KEY="deleted-user-key_";

    /**
     * 标记用户是否已经被锁定
     */
    public static final String ACCOUNT_LOCK_KEY="account-lock-key_";

    /**
     * 用户权鉴缓存 key
     */
    public static final String IDENTIFY_CACHE_KEY="shiro-cache:org.bank.shiro.CustomRealm.authorizationCache:";

    /**
     * outh2.0accessToken key
     */
    public static final String OUTH_ACCESSTOKEN_KEY="outh_accessToken_key_";

    /**
     * outh2.0code key
     */
    public static final String OUTH_CODE_KEY="outh_code_key_";

    /**
     * outh正常token
     */
    public static final String OUTH_ACCESS_TOKEN="access_token";
    public static final String OUTH_TOKEN="oauth_token";

    public static final String INVALID_CLIENT_DESCRIPTION = "客户端验证失败，如错误的client_id/client_secret。";

    public static final String PASSWORD_OR_USERNAME_ERROR = "账号密码验证失败";
}
