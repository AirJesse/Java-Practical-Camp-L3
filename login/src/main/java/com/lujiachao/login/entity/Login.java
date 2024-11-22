package com.lujiachao.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户token信息表
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@Getter
@Setter
@TableName("sys_login")
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * tokenId
     */
    private String tokenId;
}
