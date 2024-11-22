package com.lujiachao.login.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@Getter
@Setter
@TableName("oauth_client_token")
public class OauthClientToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tokenId;

    private byte[] token;

    @TableId("authentication_id")
    private String authenticationId;

    private String userName;

    private String clientId;
}
