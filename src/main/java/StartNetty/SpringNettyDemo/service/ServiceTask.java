package StartNetty.SpringNettyDemo.service;

import StartNetty.SpringNettyDemo.message.AbstractMessage;

/**
 * Created by nyq on 2017/3/6.
 */
public class ServiceTask implements Runnable {
    private AbstractMessage abstractMessage;

    public ServiceTask(AbstractMessage msg) {
        this.abstractMessage = msg;
    }

    @Override
    public void run() {
        System.out.println("execute service task " + abstractMessage);
    }
}
