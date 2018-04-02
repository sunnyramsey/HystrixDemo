package com.ramsey.hystrix.failure;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import static org.junit.Assert.*;

public class ObservableCommandHelloFailureTest {

    @Test
    public void testHotObservable() throws Exception {

        Observable<String> obWorld = new ObservableCommandHelloFailure("World").observe();



        obWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("here");
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext["+ System.currentTimeMillis()+"]: " + v);
            }

        });

    }

}