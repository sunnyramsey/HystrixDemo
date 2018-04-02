package com.ramsey.hystrix.collapser;

import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.observers.TestSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ObservableCommandCollapserGetValueTest {

    private HystrixRequestContext ctx;

    @Before
    public void before()
    {
        ctx = HystrixRequestContext.initializeContext();
    }

    @After
    public void after()
    {
        System.out.println(HystrixRequestLog.getCurrentRequest().getExecutedCommandsAsString());
        ctx.shutdown();
    }


    @Test
    public void shouldCollapseRequestsSync()
    {
        final int requestNum = 10;
        final Map<Integer, TestSubscriber<String>> subscribersByNumber = new HashMap<Integer, TestSubscriber<String>>(
                requestNum);

        TestSubscriber<String> subscriber;
        for (int number = 0; number < requestNum; number++)
        {
            subscriber = new TestSubscriber<String>();
            new ObservableCommandCollapserGetValue(number).toObservable().subscribe(subscriber);
            subscribersByNumber.put(number, subscriber);

            // wait a little bit after running half of the requests so that we don't collapse all of them into one batch
            if (number == requestNum / 2){
                try
                {
                    Thread.sleep(1000);
                }
                catch (final InterruptedException e)
                {
                    throw new IllegalStateException(e);
                }
            }

        }

        assertThat(subscribersByNumber.size(), is(requestNum));

        for (final Map.Entry<Integer, TestSubscriber<String>> subscriberByNumber : subscribersByNumber.entrySet())
        {
            subscriber = subscriberByNumber.getValue();
            subscriber.awaitTerminalEvent(10, TimeUnit.SECONDS);

            assertThat(subscriber.getOnErrorEvents().toString(), subscriber.getOnErrorEvents().size(), is(0));
            assertThat(subscriber.getOnNextEvents().size(), is(1));

            final String word = subscriber.getOnNextEvents().get(0);
            System.out.println("Translated " + subscriberByNumber.getKey() + " to " + word);

        }

    }

}