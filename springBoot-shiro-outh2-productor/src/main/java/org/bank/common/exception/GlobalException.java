package org.bank.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.bank.common.util.DataResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    /**
     * 参数校验异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        // 所有参数异常信息
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        return DataResult.getResult(-1,allErrors.get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public DataResult handlerBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();

        // 所有参数异常信息
        List<ObjectError> allErrors = bindingResult.getAllErrors();

        return DataResult.getResult(-1,allErrors.get(0).getDefaultMessage());
    }
}
