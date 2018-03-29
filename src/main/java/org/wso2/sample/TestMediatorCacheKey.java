package org.wso2.sample;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the
 */
public class TestMediatorCacheKey implements Serializable {

    private static final long serialVersionUID = -892035672962834132L;
    private String cacheId;

    public TestMediatorCacheKey(String cacheId) {
        this.cacheId = cacheId;
    }

    public String getCacheId() {
        return cacheId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cacheId);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TestMediatorCacheKey that = (TestMediatorCacheKey) obj;

        return cacheId.equals(that.cacheId);
    }
}
