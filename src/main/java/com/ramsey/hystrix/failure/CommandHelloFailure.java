package com.ramsey.hystrix.failure;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;

public class CommandHelloFailure extends HystrixCommand<String> {
    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        throw new RuntimeException("this command always fails");
        //throw new HystrixBadRequestException("Invalid Argument",new IllegalArgumentException());
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
}
