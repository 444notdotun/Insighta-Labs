package com.apiintegration.hngstage1profileaggregator.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class KeyStore {

        private final Map<String, String> store = new ConcurrentHashMap<>();
        private final Map<String, Long> expiry = new ConcurrentHashMap<>();
        private static final long TTL_MS = 120_000L;

        public void store(String key, String code) {
            store.put(key, code);
            expiry.put(key, System.currentTimeMillis() + TTL_MS);
        }

        public String retrieve(String key) {
            Long exp = expiry.get(key);
            if (exp == null || System.currentTimeMillis() > exp) {
                store.remove(key);
                expiry.remove(key);
                return null;
            }
            return store.remove(key);
        }

}
