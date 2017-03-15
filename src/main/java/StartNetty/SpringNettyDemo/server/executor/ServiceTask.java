package StartNetty.SpringNettyDemo.server.executor;

import StartNetty.SpringNettyDemo.message.AbstractMessage;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.message.struct.Request;

import java.lang.reflect.Method;

/**
 * Created by nyq on 2017/3/6.
 */
public class ServiceTask implements Runnable {
    private AbstractMessage abstractMessage;
    private OnTaskComplete onTaskComplete;

    public ServiceTask(AbstractMessage msg, OnTaskComplete onTaskComplete) {
        this.abstractMessage = msg;
        this.onTaskComplete = onTaskComplete;
    }

    @Override
    public void run() {
        try {
            Object result = handle();
            onTaskComplete.success(result);
        }
        catch (Exception e) {
            onTaskComplete.fail(e);
        }

    }

    private Object handle() throws Exception {
        NettyMessage nettyMessage = (NettyMessage) abstractMessage;
        Request request = (Request) nettyMessage.getAttachment();
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Object handler = ServicePool.getHandlerMap().get(className);
        Method method = handler.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(handler, parameters);
    }
}
