package com.ramsey.hystrix.basic;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandUsingRequestCacheTest {


    @Test
    public void testWithCacheHits() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache commandA = new CommandUsingRequestCache(2);
            CommandUsingRequestCache commandB = new CommandUsingRequestCache(2);

            assertTrue(commandA.execute());
            // this is the first time we've executed this command with the value of "2" so it should not be from cache
            assertFalse(commandA.isResponseFromCache());

            assertTrue(commandB.execute());
            // this is the second time we've executed this command with the same value so it should return from cache
            assertTrue(commandB.isResponseFromCache());
        } finally {
            context.shutdown();
        }

        // start a new request context
        context = HystrixRequestContext.initializeContext();
        try {
            CommandUsingRequestCache commandC = new CommandUsingRequestCache(2);
            assertTrue(commandC.execute());
            // this is a new request context so this should not come from cache
            assertFalse(commandC.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }

}