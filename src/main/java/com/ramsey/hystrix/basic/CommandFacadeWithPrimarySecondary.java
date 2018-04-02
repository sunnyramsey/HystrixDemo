package com.ramsey.hystrix.basic;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.*;

public class CommandFacadeWithPrimarySecondary extends HystrixCommand<String> {

    //netflix dynamic property
    private final static DynamicBooleanProperty usePrimary = DynamicPropertyFactory.getInstance().getBooleanProperty("primarySecondary.usePrimary", true);


    public CommandFacadeWithPrimarySecondary() {
        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("PrimarySecondary"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("PrimarySecondaryCommand"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
    }

    @Override
    protected String run() {
        if (usePrimary.get()) {
            return new PrimaryCommand().execute();
        } else {
            return new SecondaryCommand().execute();
        }
    }

    @Override
    protected String getFallback() {
        return null;
    }


    private static class PrimaryCommand extends HystrixCommand<String> {


        private PrimaryCommand() {
            super(Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("PrimarySecondary"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("PrimaryCommand"))
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("PrimaryCommand")));
        }

        @Override
        protected String run() {
            // perform expensive 'primary' service call
            return "responseFromPrimary";
        }

    }

    private static class SecondaryCommand extends HystrixCommand<String> {

        private SecondaryCommand() {
            super(Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("PrimarySecondary"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("SecondaryCommand"))
                    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("SecondaryCommand")));
        }

        @Override
        protected String run() {
            // perform fast 'secondary' service call
            return "responseFromSecondary";
        }

    }


}
