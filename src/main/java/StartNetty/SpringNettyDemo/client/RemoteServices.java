package StartNetty.SpringNettyDemo.client;

import StartNetty.SimpleNettyProtocol.Client.ServiceReqHandler;
import StartNetty.SpringNettyDemo.config.BaseChannelInitializer;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.message.struct.Request;
import StartNetty.SpringNettyDemo.services.HelloService;
import StartNetty.SpringNettyDemo.services.Person;
import StartNetty.SpringNettyDemo.services.Service;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nyq on 2017/3/13.
 */

@Component
public class RemoteServices {

    private static ApplicationContext ctx;
    private final ConcurrentHashMap<String, ChannelFuture> map = new ConcurrentHashMap<>();
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;

    @Autowired
    public RemoteServices(ApplicationContext ctx) {
        RemoteServices.ctx = ctx;
        init();
    }

    public static RemoteServices getInstance() {
        return ctx.getBean(RemoteServices.class);
    }

    public void init() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new BaseChannelInitializer(ctx));
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface},
                new ServiceProxy<T>(serviceInterface));
    }

    public <T> AsyncServiceProxy createAsync(Class<T> serviceInterface) {
        return new ServiceProxy<T>(serviceInterface);
    }

    public ResFuture send(String address, NettyMessage nettyMessage) throws InterruptedException {
        ChannelFuture f = connect(address);
        return f.channel().pipeline().get(ServiceHandler.class).sendMessage(nettyMessage);
    }

    public ChannelFuture connect(String address) throws InterruptedException {
        if (map.get(address) == null) {
            synchronized (map) {
                if (map.get(address) == null) {
                    String[] host_port = address.split(":");
                    ChannelFuture f = bootstrap.connect(host_port[0], Integer.parseInt(host_port[1])).sync();
                    map.put(address, f);
                }
            }
        }
        return map.get(address);
    }

    public void stop () throws InterruptedException {
        group.shutdownGracefully().sync();
    }

}
