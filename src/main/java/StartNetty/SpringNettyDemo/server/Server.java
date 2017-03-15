package StartNetty.SpringNettyDemo.server;

import StartNetty.SpringNettyDemo.config.BaseChannelInitializer;
import StartNetty.SpringNettyDemo.server.servicesImpl.RPCService;
import StartNetty.SpringNettyDemo.server.executor.ServicePool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nyq on 2017/3/5.
 */

@Component
public class Server  {

    @Value("${server.port}")
    private String port;

    private ApplicationContext ctx;

    @Autowired
    public Server(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new BaseChannelInitializer(ctx));

        Map<String, Object> handlerMap = ctx.getBeansWithAnnotation(RPCService.class);
        if (handlerMap != null) {
            ConcurrentHashMap map = ServicePool.getHandlerMap();
            handlerMap.forEach((name, bean)->{
                map.put(bean.getClass().getAnnotation(RPCService.class).value().getName(), bean);
            });
        }

        try {
            ChannelFuture f = serverBootstrap.bind(Integer.parseInt(port)).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static class StartServer {
        public static void main(String[] args) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("server-spring.xml");
            ctx.getBean(Server.class).start();
        }
    }
}
