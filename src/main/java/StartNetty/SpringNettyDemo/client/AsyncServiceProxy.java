package StartNetty.SpringNettyDemo.client;

/**
 * Created by nyq on 2017/3/14.
 */
public interface AsyncServiceProxy {
    RPCFuture call(String methodName, Object...args);
}
