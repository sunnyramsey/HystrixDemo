package com.ramsey.hystrix.basic;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

public class CommandUsingRequestCacheInvalidation {

    //simulate remote storage.
    private static volatile String prefixStoredOnRemoteDataStore = "Cache";

    public static class GetterCommand extends HystrixCommand<String> {

        private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("GetterCommand");
        private final int num;

        public GetterCommand(int num) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetSetGet"))
                    .andCommandKey(GETTER_KEY));
            this.num = num;
        }

        @Override
        protected String run() {
            return prefixStoredOnRemoteDataStore + num;
        }

        @Override
        protected String getCacheKey() {
            return String.valueOf(num);
        }

        public static void flushCache(int num) {
            HystrixRequestCache.getInstance(GETTER_KEY,
                    HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(num));
        }

    }

    public static class SetterCommand extends HystrixCommand<Void> {

        private final int num;
        private final String prefix;

        public SetterCommand(int num, String prefix) {
            super(HystrixCommandGroupKey.Factory.asKey("GetSetGet"));
            this.num = num;
            this.prefix = prefix;
        }

        @Override
        protected Void run() {
            prefixStoredOnRemoteDataStore = prefix;
            GetterCommand.flushCache(num);
            return null;
        }
    }
}
