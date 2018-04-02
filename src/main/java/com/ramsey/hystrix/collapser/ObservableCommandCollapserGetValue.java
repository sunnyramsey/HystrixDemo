package com.ramsey.hystrix.collapser;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixObservableCollapser;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableCommandCollapserGetValue extends HystrixObservableCollapser<Integer,String,String,Integer> {

    private final Integer num;

    public ObservableCommandCollapserGetValue(Integer num){
        this.num = num;
    }

    @Override
    public Integer getRequestArgument() {
        return num;
    }

    @Override
    protected HystrixObservableCommand<String> createCommand(Collection<HystrixCollapser.CollapsedRequest<String, Integer>> collapsedRequests) {
        System.out.println("Creating batch for " + collapsedRequests.size() + " requests.");

        final List<Integer> numbers = new ArrayList<Integer>();
        for (final HystrixCollapser.CollapsedRequest<String, Integer> request : collapsedRequests)
        {
            numbers.add(request.getArgument());
        }

        return new ObservableCommandGetValue(numbers);
    }

    @Override
    protected Func1<String, Integer> getBatchReturnTypeKeySelector() {
        return new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return Integer.parseInt(s);
            }
        };
    }

    @Override
    protected Func1<Integer, Integer> getRequestArgumentKeySelector() {
        return new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer;
            }
        };
    }

    @Override
    protected void onMissingResponse(HystrixCollapser.CollapsedRequest<String, Integer> r) {
        r.setException(new Exception("Missing."));
    }

    @Override
    protected Func1<String, String> getBatchReturnTypeToResponseTypeMapper() {
        return new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s;
            }
        };
    }


}
