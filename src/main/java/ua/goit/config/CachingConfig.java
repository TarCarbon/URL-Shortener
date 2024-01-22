package ua.goit.config;

import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;

import static java.util.Arrays.*;

@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public SimpleCacheManager simpleCacheManager(Set<Cache> caches) {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        simpleCacheManager.setCaches(caches);

        return simpleCacheManager;
    }

    @Bean(name = "urls")
    public ConcurrentMapCacheFactoryBean getUrlsCache() {
        return new ConcurrentMapCacheFactoryBean();
    }

    @Bean(name = "active_urls")
    public ConcurrentMapCacheFactoryBean getActiveUrlsCache() {
        return new ConcurrentMapCacheFactoryBean();
    }

    @Bean(name = "inactive_urls")
    public ConcurrentMapCacheFactoryBean getInactiveUrlsCache() {
        return new ConcurrentMapCacheFactoryBean();
    }
}
