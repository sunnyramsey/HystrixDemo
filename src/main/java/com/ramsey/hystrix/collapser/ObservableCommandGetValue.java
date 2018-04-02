package com.ramsey.hystrix.collapser;


import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.util.List;

public class ObservableCommandGetValue extends HystrixObservableCommand<String> {

    private List<Integer> nums;

    public ObservableCommandGetValue(List<Integer> nums){
        super(HystrixCommandGroupKey.Factory.asKey("collapser"));
        this.nums = nums;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.from(nums).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return Integer.toString(integer);
            }
        });
    }




}
