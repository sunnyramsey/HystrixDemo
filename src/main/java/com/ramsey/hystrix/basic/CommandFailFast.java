package com.ramsey.hystrix.basic;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandFailFast extends HystrixCommand<String> {

    private final boolean throwException;

    public CommandFailFast(boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("FailFast"));
        this.throwException = throwException;
    }

    @Override
    protected String run() {
        if (throwException) {
            throw new RuntimeException("failure from CommandFailFast");
        } else {
            return "success";
        }
    }
}
