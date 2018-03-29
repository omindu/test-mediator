package org.wso2.sample.mediator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.wso2.sample.mediator.cache.TestMediatorCache;
import org.wso2.sample.mediator.cache.TestMediatorCacheEntry;
import org.wso2.sample.mediator.cache.TestMediatorCacheKey;

import java.util.UUID;

/**
 * This mediator demonstrate adding and retrieving entries from a cache implementation.
 */
public class TestMediator extends AbstractMediator {

    private TestMediatorCache testMediatorCache = TestMediatorCache.getInstance();
    private static String ID = UUID.randomUUID().toString();
    private static String VALUE = "Some Value";
    private static final Log log = LogFactory.getLog(TestMediator.class);

    public boolean mediate(MessageContext context) {

        TestMediatorCacheKey key = new TestMediatorCacheKey(ID);
        // Get the value from cache.
        TestMediatorCacheEntry value = testMediatorCache.getValueFromCache(key);

        if (value == null) {
            // Add to cache if the entry is not in the cache.
            if (log.isDebugEnabled()) {
                log.debug("Cache entry not found for key: " + key);
            }
            value = new TestMediatorCacheEntry(VALUE);
            testMediatorCache.addToCache(key, value);
            if (log.isDebugEnabled()) {
                log.debug("Added to cache: " + ID + ", " + VALUE);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Found in cache: " + value.getCacheEntry());
            }
        }

        return true;
    }
}
