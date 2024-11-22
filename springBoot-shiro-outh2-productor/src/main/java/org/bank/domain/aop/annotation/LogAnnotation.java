package org.bank.domain.aop.annotation;

import java.lang.annotation.*;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.aop.annotation
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /** 模块 */
    String title() default "";

    /** 功能 */
    String action() default "";
}
