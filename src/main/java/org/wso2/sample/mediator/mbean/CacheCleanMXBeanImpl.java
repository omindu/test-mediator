package org.wso2.sample.mediator.mbean;

import org.wso2.carbon.context.PrivilegedCarbonContext;

import javax.cache.CacheManager;
import javax.cache.Caching;

import static org.wso2.sample.mediator.cache.TestMediatorCache.CACHE_MANAGER_NAME;
import static org.wso2.sample.mediator.cache.TestMediatorCache.CACHE_NAME;
import static org.wso2.sample.mediator.cache.TestMediatorCache.SUPER_TENANT_DOMAIN;

public class CacheCleanMXBeanImpl implements CacheCleanMXBean {

    @Override
    public void clear() throws Exception {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantDomain(SUPER_TENANT_DOMAIN, true);
        try {
            //get the governance registry manager abn the cache
            CacheManager cacheManager = Caching.getCacheManagerFactory().getCacheManager(CACHE_MANAGER_NAME);
            //invalidate all entries of the registry
            cacheManager.getCache(CACHE_NAME).removeAll();
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

    }
}