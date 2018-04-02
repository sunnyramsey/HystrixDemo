package com.ramsey.hystrix.basic;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandUsingRequestCacheInvalidationTest {

    @Test
    public void getGetSetGet() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            assertEquals("Cache1", new CommandUsingRequestCacheInvalidation.GetterCommand(1).execute());
            CommandUsingRequestCacheInvalidation.GetterCommand commandAgainstCache = new CommandUsingRequestCacheInvalidation.GetterCommand(1);
            assertEquals("Cache1", commandAgainstCache.execute());

            assertTrue(commandAgainstCache.isResponseFromCache());


            CommandUsingRequestCacheInvalidation.GetterCommand commandAfterSet = new CommandUsingRequestCacheInvalidation.GetterCommand(1);

            assertFalse(commandAfterSet.isResponseFromCache());
            assertEquals("Cache1", commandAfterSet.execute());
        } finally {
            context.shutdown();
        }
    }

}