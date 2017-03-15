package StartNetty.SpringNettyDemo.client;

import java.util.concurrent.TimeUnit;

/**
 * Created by nyq on 2017/3/14.
 */
public interface RPCFuture {
    RPCFuture addListener(OnReceiveListener listener);
    Object get() throws Exception;
    Object get(long timeout, TimeUnit timeUnit) throws Exception;
}
