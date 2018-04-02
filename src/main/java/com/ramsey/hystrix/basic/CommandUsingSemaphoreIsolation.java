package com.ramsey.hystrix.basic;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class CommandUsingSemaphoreIsolation extends HystrixCommand<String> {

    public CommandUsingSemaphoreIsolation(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Semaphore"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));
    }

    @Override
    protected String run() {
        // a real implementation would retrieve data from in memory data structure
        // or some other similar non-network involved work
        return "Semaphore";
    }
}
