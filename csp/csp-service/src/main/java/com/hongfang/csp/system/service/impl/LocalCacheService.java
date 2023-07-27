package com.hongfang.csp.system.service.impl;

import java.util.Map;

/**
 * 用來儲存後端的 uuid 和 SQL 語句的對應
 * @author yfwong
 * @date 2023/07/25
 */
public class LocalCacheService {
    public static java.util.Map<String, Object> cache = new java.util.HashMap<String, Object>();
    public static Map<String, Object> localCache() {
        return cache;
    }

    // Put data in global cache variable
    public static void putCache(String key, Object value) {
        cache.put(key, value);
    }

    public static void clear() {
        cache.clear();
    }
}
