package StartNetty.SpringNettyDemo.service;

import StartNetty.SpringNettyDemo.message.AbstractMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nyq on 2017/3/6.
 */
public class ServicePool {
    private final static ExecutorService executorService = Executors.newCachedThreadPool();
    private ServicePool() {}


    public void execute(ServiceTask task) {
        executorService.submit(task);
    }

    public void execute(AbstractMessage abstractMessage) {
        execute(new ServiceTask(abstractMessage));
    }

    private static final class Holder { private static final ServicePool INSTANCE = new ServicePool(); }

    public static ServicePool getInstance() {
        return Holder.INSTANCE;
    }
}
