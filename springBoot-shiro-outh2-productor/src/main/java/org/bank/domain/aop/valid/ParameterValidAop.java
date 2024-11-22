package org.bank.domain.aop.valid;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Component
@Aspect
public class ParameterValidAop {
    @Before("@annotation(paramValid)")
    public void paramValid(JoinPoint point, ParamValid paramValid) throws Exception {
        Object[] paramObj = point.getArgs();
        for (Object obj : paramObj) {
            if (obj instanceof BindingResult) {
                BindingResult result = (BindingResult) obj;
                if (result.hasErrors()) {
                    List<ObjectError> allErrors = result.getAllErrors();
                    //返回第一个错误
                    String defaultMessage = allErrors.get(0).getDefaultMessage();
                    throw new Exception(defaultMessage);
                }
            }
        }
    }
}
