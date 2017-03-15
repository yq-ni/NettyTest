package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.message.struct.Request;
import StartNetty.SpringNettyDemo.services.Service;
import io.netty.channel.ChannelFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by nyq on 2017/3/14.
 */
public class ServiceProxy<T> implements InvocationHandler, AsyncServiceProxy {
    private Class<T> clazz;

    public ServiceProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public RPCFuture call(String methodName, Object... args) {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i=0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        String address = clazz.getAnnotation(Service.class).address();
        NettyMessage nettyMessage = buildRequest(clazz.getName(), methodName, parameterTypes, args);
        try {
            return RemoteServices.getInstance().send(address, nettyMessage);
        }
        catch (InterruptedException e) {

        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> interfaceClass = method.getDeclaringClass();
        NettyMessage nettyMessage = buildRequest(interfaceClass.getName(),
                                        method.getName(), method.getParameterTypes(), args);
        String address = interfaceClass.getAnnotation(Service.class).address();
        ResFuture resFuture = RemoteServices.getInstance().send(address, nettyMessage);
        return resFuture.get();
    }

    private NettyMessage buildRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Request request = new Request();
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(parameters);
        NettyMessage n = new NettyMessage();
        n.setType(MessageType.SERVICE_REQ.value);
        n.setAttachment(request);
        return n;
    }

    private static Class<?> getClassType(Object obj){
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName){
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }

        return classType;
    }
}
