package org.bank.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @CreateTime: 2022-06-11  14:00
 * @Description:http上下文
 * @Version: 1.0
 */
public class HttpContextUtils {
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * @description:判断是否是 ajax/app请求
     * @author: lizongle
     * @date:  2022/6/11  14:00
     * @param: [request]
     * @return: boolean
     **/
    public  static boolean isAjaxRequest(HttpServletRequest request){

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1
                || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        ));
    }
}
