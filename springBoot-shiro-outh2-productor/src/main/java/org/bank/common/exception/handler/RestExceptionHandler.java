package org.bank.common.exception.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.DataResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.exception.handler
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {


    /**
     * @description:系统繁忙，请稍候再试
     * @author: lizongle
     * @param: [e]
     * @return: org.bank.utils.DataResult
     **/
    @ExceptionHandler(Exception.class)
    public <T> DataResult<T> handleException(Exception e){
        log.error("Exception,exception:{}", e);
        return DataResult.getResult(BaseResponseCode.SYSTEM_BUSY);
    }


    /**
     * @description:自定义全局异常处理
     * @author: lizongle
     * @param: [e]
     * @return: org.bank.utils.DataResult
     **/
    @ExceptionHandler(value = BusinessException.class)
    <T> DataResult<T> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException,exception:{}", e);
        return new DataResult<>(e.getMessageCode(),e.getDetailMessage());
    }

    /**
     * @description:没有权限 返回403视图
     * @author: lizongle
     * @param: [e]
     * @return: org.bank.utils.DataResult
     **/
    @ExceptionHandler(value = AuthorizationException.class)
    public <T> DataResult<T>  erroPermission(AuthorizationException e){
        log.error("BusinessException,exception:{}", e);
        return new DataResult<>(BaseResponseCode.UNAUTHORIZED_ERROR);

    }

    /**
     * @description:处理validation 框架异常
     * @author: lizongle
     * @param: [e]
     * @return: org.bank.utils.DataResult
     **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }
    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}",msgs[i]);
            i++;
        }
        return DataResult.getResult(BaseResponseCode.METHODARGUMENTNOTVALIDEXCEPTION.getCode(), msgs[0]);
    }



}
