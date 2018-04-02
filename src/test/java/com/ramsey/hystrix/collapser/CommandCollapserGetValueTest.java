package com.ramsey.hystrix.collapser;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class CommandCollapserGetValueTest {

    @Test
    public void testCollapser() throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            Future<String> f1 = new CommandCollapserGetValue(1).queue();
            Future<String> f2 = new CommandCollapserGetValue(2).queue();
            Future<String> f3 = new CommandCollapserGetValue(3).queue();
            Future<String> f4 = new CommandCollapserGetValue(4).queue();

            assertEquals("ValueForKey: 1", f1.get());
            assertEquals("ValueForKey: 2", f2.get());
            assertEquals("ValueForKey: 3", f3.get());
            assertEquals("ValueForKey: 4", f4.get());


            assertEquals(1, HystrixRequestLog.getCurrentRequest().getExecutedCommands().size());
            HystrixCommand<?> command = HystrixRequestLog.getCurrentRequest().getExecutedCommands().toArray(new HystrixCommand<?>[1])[0];

            assertEquals("GetValueForKey", command.getCommandKey().name());
            assertTrue(command.getExecutionEvents().contains(HystrixEventType.COLLAPSED));

        } finally {
            context.shutdown();
        }
    }

}