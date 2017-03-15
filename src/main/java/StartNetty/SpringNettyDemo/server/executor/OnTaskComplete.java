package StartNetty.SpringNettyDemo.server.executor;

/**
 * Created by nyq on 2017/3/13.
 */
public interface OnTaskComplete {
    void success(Object result);
    void fail(Object error);
}
