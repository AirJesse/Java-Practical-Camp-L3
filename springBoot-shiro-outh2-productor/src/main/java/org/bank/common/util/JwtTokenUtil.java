package org.bank.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bank.common.constant.Constant;
import org.springframework.util.StringUtils;
import javax.xml.bind.DatatypeConverter;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @Description: Jwt工具类
 * @Version: 1.0
 */
@Slf4j
public class JwtTokenUtil {
    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String issuer;

    public static void setTokenSettings(TokenSettings tokenSettings) {
        secretKey = tokenSettings.getSecretKey();
        accessTokenExpireTime = tokenSettings.getAccessTokenExpireTime();
        refreshTokenExpireTime = tokenSettings.getRefreshTokenExpireTime();
        refreshTokenExpireAppTime = tokenSettings.getRefreshTokenExpireAppTime();
        issuer = tokenSettings.getIssuer();
    }

    /**
     * @description:生成 access_token
     * @author: lizongle
     * @param: [subject, claims]
     * @return: java.lang.String
     **/
    public static String getAccessToken(String subject, Map<String, Object> claims) {

        return generateToken(issuer, subject, claims, accessTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * @description:生成 App端 refresh_token
     * @author: lizongle
     * @param: [subject, claims]
     * @return: java.lang.String
     **/
    public static String getRefreshAppToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, refreshTokenExpireAppTime.toMillis(), secretKey);
    }

    /**
     * @description: 生成 PC refresh_token
     * @author: lizongle
     * @param: [subject, claims]
     * @return: java.lang.String
     **/

    public static String getRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, refreshTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * @description:签发token
     * @author: lizongle
     * @param: [issuer, subject, claims, ttlMillis, secret]
     * @return: java.lang.String
     **/
    public static String generateToken(String issuer, String subject, Map<String, Object> claims, long ttlMillis, String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        if (null != claims) {
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * @description:获取用户id
     * @author: lizongle
     * @param: [token]
     * @return: java.lang.String
     **/
    public static String getUserId(String token) {
        String userId = null;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("eror={}", e);
        }
        return userId;
    }

    /**
     * @description:获取用户名
     * @author: lizongle
     * @param: [token]
     * @return: java.lang.String
     **/
    public static String getUserName(String token) {

        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(Constant.JWT_USER_NAME);
        } catch (Exception e) {
            log.error("eror={}", e);
        }
        return username;
    }

    /**
     * @description:从令牌中获取数据声明
     * @author: lizongle
     * @param: [token]
     * @return: io.jsonwebtoken.Claims
     **/
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * @description:校验令牌
     * @author: lizongle
     * @param: [token]
     * @return: java.lang.Boolean
     **/
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null != claimsFromToken && !isTokenExpired(token));
    }

    /**
     * @description:验证token 是否过期
     * @author: lizongle
     * @param: [token]
     * @return: java.lang.Boolean
     **/
    public static Boolean isTokenExpired(String token) {

        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("error={}", e);
            return true;
        }
    }

    /**
     * @description:刷新token
     * @author: lizongle
     * @param: [refreshToken, claims]
     * @return: java.lang.String
     **/
    public static String refreshToken(String refreshToken, Map<String, Object> claims) {
        String refreshedToken;
        try {
            Claims parserclaims = getClaimsFromToken(refreshToken);
            /**
             * 刷新token的时候如果为空说明原先的 用户信息不变 所以就引用上个token里的内容
             */
            if (null == claims) {
                claims = parserclaims;
            }
            refreshedToken = generateToken(parserclaims.getIssuer(), parserclaims.getSubject(), claims, accessTokenExpireTime.toMillis(), secretKey);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("error={}", e);
        }
        return refreshedToken;
    }

    /**
     * @description:获取token的剩余过期时间
     * @author: lizongle
     * @param: [token]
     * @return: long
     **/
    public static long getRemainingTime(String token) {
        long result = 0;
        try {
            long nowMillis = System.currentTimeMillis();
            result = getClaimsFromToken(token).getExpiration().getTime() - nowMillis;
        } catch (Exception e) {
            log.error("error={}", e);
        }
        return result;
    }

    public static void main(String[] args) {
    }

}
