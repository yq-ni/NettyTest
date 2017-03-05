package StartNetty.SpringNettyDemo.server;

import StartNetty.SpringNettyDemo.config.BaseInitializer;
import StartNetty.SpringNettyDemo.config.ServerHandler;
import StartNetty.SpringNettyDemo.message.codec.MessageDecoder;
import StartNetty.SpringNettyDemo.message.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

/**
 * Created by nyq on 2017/3/5.
 */
public class Server implements ApplicationContextAware, InitializingBean{

    private ApplicationContext ctx;
    private final int PORT;

    public Server(int port) {
        PORT = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void start() {
        final Map<String, Object> map = ctx.getBeansWithAnnotation(ServerHandler.class);
        LinkedList<Object> handlers = new LinkedList<Object>(map.values());
        handlers.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((ServerHandler)o1).order() - ((ServerHandler)o2).order();
            }
        });
        handlers.addFirst(ctx.getBean(MessageDecoder.class));
        handlers.addFirst(ctx.getBean(MessageEncoder.class));

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new BaseInitializer(handlers));

        try {
            ChannelFuture f = serverBootstrap.bind(PORT).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
