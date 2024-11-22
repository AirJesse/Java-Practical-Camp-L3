package org.bank.common.util;


import org.springframework.stereotype.Component;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Component
public class StaticInitializerUtil {
   private TokenSettings tokenSettings;
    public StaticInitializerUtil(TokenSettings tokenSettings) {
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
