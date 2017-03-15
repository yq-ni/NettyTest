package StartNetty.SpringNettyDemo.server.executor;

import StartNetty.SpringNettyDemo.message.AbstractMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nyq on 2017/3/6.
 */
public class ServicePool {
    private ExecutorService executorService;
    private final static ConcurrentHashMap<String, Object> HANDLER_MAP = new ConcurrentHashMap<>();

    private ServicePool() {
        executorService = Executors.newCachedThreadPool();
    }


    public void execute(ServiceTask task) {
        executorService.execute(task);
    }

    public void execute(AbstractMessage abstractMessage, OnTaskComplete onTaskComplete) {
        execute(new ServiceTask(abstractMessage, onTaskComplete));
    }

    private static final class Holder { private static final ServicePool INSTANCE = new ServicePool(); }

    public static ServicePool getInstance() {
        return Holder.INSTANCE;
    }

    public static ConcurrentHashMap getHandlerMap () {
        return HANDLER_MAP;
    }
}
