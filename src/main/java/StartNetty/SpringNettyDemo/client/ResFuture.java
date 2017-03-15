package StartNetty.SpringNettyDemo.client;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by nyq on 2017/3/12.
 */
public class ResFuture implements RPCFuture {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private Object result;
    private List<OnReceiveListener> listeners = new LinkedList<>();

    public ResFuture() {
    }

    public synchronized void done(Object result) {
        this.result = result;
        for (OnReceiveListener listener : listeners) {
            listener.onReceive(result);
        }
        countDownLatch.countDown();
    }

    @Override
    public synchronized RPCFuture addListener(OnReceiveListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public Object get() throws InterruptedException {
        countDownLatch.await();
        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException {
        countDownLatch.await(timeout, unit);
        return result;
    }
}
