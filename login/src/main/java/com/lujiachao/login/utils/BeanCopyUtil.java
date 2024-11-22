package com.lujiachao.login.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BeanCopyUtil {
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null) return null;
        T target = null;
        try {
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T> List<T> copyList(List<?> list, Class<T> targetClass) {
        List<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        list.forEach(source -> {
            result.add(copy(source, targetClass));
        });
        return result;
    }
}
