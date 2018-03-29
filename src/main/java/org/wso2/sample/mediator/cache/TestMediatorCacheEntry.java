package org.wso2.sample.mediator.cache;

import java.io.Serializable;

public class TestMediatorCacheEntry implements Serializable {

    private static final long serialVersionUID = -571956809921676585L;
    private String cacheEntry;

    public TestMediatorCacheEntry(String cacheEntry) {
        this.cacheEntry = cacheEntry;
    }

    public String getCacheEntry() {
        return cacheEntry;
    }
}
