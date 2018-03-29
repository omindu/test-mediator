package org.wso2.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.caching.impl.CacheImpl;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.ManagementFactory;

import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheConfiguration;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class TestMediatorCache {

    public static final String CACHE_MANAGER_NAME = "TEST_MEDIATOR_CACHE_MANAGER";
    public static final String CACHE_NAME = "TEST_MEDIATOR_CACHE";
    public static final String SUPER_TENANT_DOMAIN = "carbon.super";

    private static final Log log = LogFactory.getLog(TestMediatorCache.class);
    private static final TestMediatorCache INSTANCE = new TestMediatorCache();
    private CacheBuilder<TestMediatorCacheKey, TestMediatorCacheEntry> cacheBuilder;


    private TestMediatorCache() {

        // An MBean is registered in the server for clearing mediator cache through JMX.
        try {
            MBeanServer mbs = ManagementFactory.getMBeanServer();
            ObjectName name = new ObjectName("org.wso2.sample:type=TestMediatorCacheClearMXBean");
            CacheCleanMXBeanImpl cacheCleanMXBean = new CacheCleanMXBeanImpl();
            mbs.registerMBean(cacheCleanMXBean, name);
            if (log.isDebugEnabled()); {
                log.debug("TestMediatorCacheClearMXBean added to the MBean server");
            }
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException |
                NotCompliantMBeanException e) {
            log.error("Error while registering mediator cache MBean.", e);
        }
    }

    public static TestMediatorCache getInstance() {

        return INSTANCE;
    }

    private Cache<TestMediatorCacheKey, TestMediatorCacheEntry> getCache() {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);
        Cache<TestMediatorCacheKey, TestMediatorCacheEntry> cache;

        try {
            CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_NAME);
            if (cacheBuilder == null) {
                synchronized (CACHE_NAME) {
                    if (cacheBuilder == null) {
                        cacheManager.removeCache(CACHE_NAME);
                        cacheBuilder = cacheManager.<TestMediatorCacheKey, TestMediatorCacheEntry>createCacheBuilder
                                (CACHE_NAME)
                                .setExpiry(CacheConfiguration.ExpiryType.ACCESSED,
                                           new CacheConfiguration.Duration(TimeUnit.SECONDS, getCacheTimeout()))
                                .setExpiry(CacheConfiguration.ExpiryType.MODIFIED,
                                           new CacheConfiguration.Duration(TimeUnit.SECONDS, getCacheTimeout()))
                                .setStoreByValue(false);
                        cache = cacheBuilder.build();
                        ((CacheImpl) cache).setCapacity(getCapacity());
                    } else {
                        cache = cacheManager.getCache(CACHE_NAME);
                        ((CacheImpl) cache).setCapacity(getCapacity());
                    }
                }
            } else {
                cache = cacheManager.getCache(CACHE_NAME);
                ((CacheImpl) cache).setCapacity(getCapacity());
            }
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return cache;
    }

    /**
     * Add entry to cache.
     * @param key Cache key.
     * @param entry Cache entry.
     */
    public void addToCache(TestMediatorCacheKey key, TestMediatorCacheEntry entry) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);

        try {
            Cache<TestMediatorCacheKey, TestMediatorCacheEntry> cache = getCache();
            if (cache != null) {
                cache.put(key, entry);
            }
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    /**
     * Get cache entry by cache key.
     * @param key Cache key.
     * @return Cache entry if exist, null otherwise.
     */
    public TestMediatorCacheEntry getValueFromCache(TestMediatorCacheKey key) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);

        if (key == null) {
            return null;
        }

        try {

            Cache<TestMediatorCacheKey, TestMediatorCacheEntry> cache = getCache();
            if (cache != null && cache.get(key) != null) {
                return cache.get(key);
            }
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        return null;
    }

    /**
     * Clear a cache entry by cache key.
     * @param key Cache key.
     */
    public void clearCacheEntry(TestMediatorCacheKey key) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);

        try {

            Cache<TestMediatorCacheKey, TestMediatorCacheEntry> cache = getCache();
            if (cache != null) {
                cache.remove(key);
            }
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

    }

    /**
     * Clear all cache entries.
     */
    public void clear() {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);
        try {

            Cache<TestMediatorCacheKey, TestMediatorCacheEntry> cache = getCache();
            if (cache != null) {
                cache.removeAll();
            }
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
    }

    private long getCacheTimeout() {

        // Adjust the timeout accordingly. The value is in milliseconds.
        return 900;
    }

    private long getCapacity() {

        // Adjust the cache capacity accordingly.
        return 1000;
    }
}
