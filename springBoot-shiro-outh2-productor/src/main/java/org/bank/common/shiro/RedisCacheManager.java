package org.bank.common.shiro;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.bank.domain.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.shiro
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisService redisService;


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache(name, redisService);
    }

}