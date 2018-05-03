package com.ramsey.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.ramsey.hystrix.basic.CommandHelloWorld;

public class Main {
    public static void main(String[] args){
        DynamicLongProperty timeToWait =
                DynamicPropertyFactory.getInstance().getLongProperty("hystrix.command.HelloWorld.execution.isolation.thread.timeoutInMilliseconds", 20000);
        System.out.println(timeToWait);
        new CommandHelloWorld("World").execute();
    }
}
