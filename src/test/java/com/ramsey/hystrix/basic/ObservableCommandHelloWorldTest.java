package com.ramsey.hystrix.basic;

import com.ramsey.hystrix.basic.ObservableCommandHelloWorld;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class ObservableCommandHelloWorldTest {

    @Test
    public void testSynchronous() {
        try {
            assertEquals("Hello World!", new ObservableCommandHelloWorld("World").observe().toBlocking().toFuture().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHotObservable() throws Exception {

        Observable<String> obWorld = new ObservableCommandHelloWorld("World").observe();
        Observable<String> obRamsey = new ObservableCommandHelloWorld("Ramsey").observe();

        Thread.sleep(3000);

        // non blocking
        obWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext["+ System.currentTimeMillis()+"]: " + v);
            }

        });

        obRamsey.subscribe(new Action1<String>() {

            @Override
            public void call(String v) {
                System.out.println("onNext["+ System.currentTimeMillis()+"]: " + v);
            }

        });
    }


    @Test
    public void testColdObservable() throws Exception {

        Observable<String> obWorld = new ObservableCommandHelloWorld("World").toObservable();
        Observable<String> obRamsey = new ObservableCommandHelloWorld("Ramsey").toObservable();

        Thread.sleep(3000);

        // non blocking
        obWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext["+ System.currentTimeMillis()+"]: " + v);
            }

        });

        obRamsey.subscribe(new Action1<String>() {

            @Override
            public void call(String v) {
                System.out.println("onNext["+ System.currentTimeMillis()+"]: " + v);
            }

        });
    }


}