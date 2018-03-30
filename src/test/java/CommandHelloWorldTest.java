import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class CommandHelloWorldTest {

    @Test
    public void testSynchronous() {
        assertEquals("Hello World!", new CommandHelloWorld("World").execute());
        assertEquals("Hello Ramsey!", new CommandHelloWorld("Ramsey").execute());
    }

    @Test
    public void testAsynchronous1() throws Exception {
        assertEquals("Hello World!", new CommandHelloWorld("World").queue().get());
        assertEquals("Hello Ramsey!", new CommandHelloWorld("Ramsey").queue().get());
    }

    @Test
    public void testAsynchronous2() throws Exception {

        Future<String> fWorld = new CommandHelloWorld("World").queue();
        Future<String> fBob = new CommandHelloWorld("Ramsey").queue();

        assertEquals("Hello World!", fWorld.get());
        assertEquals("Hello Ramsey!", fBob.get());
    }

    @Test
    public void testHotObservable() throws Exception {

        Observable<String> obWorld = new CommandHelloWorld("World").observe();
        Observable<String> obRamsey = new CommandHelloWorld("Ramsey").observe();


        // blocking
        assertEquals("Hello World!", obWorld.toBlocking().single());
        assertEquals("Hello Ramsey!", obRamsey.toBlocking().single());

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
                System.out.println("onNext: " + v);
            }

        });

        obRamsey.subscribe(new Action1<String>() {

            @Override
            public void call(String v) {
                System.out.println("onNext: " + v);
            }

        });
    }



}