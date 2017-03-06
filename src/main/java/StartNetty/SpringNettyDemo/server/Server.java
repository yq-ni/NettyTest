package StartNetty.SpringNettyDemo.server;

import StartNetty.SpringNettyDemo.config.ServerHandler;
import StartNetty.SpringNettyDemo.message.codec.MessageDecoder;
import StartNetty.SpringNettyDemo.message.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

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
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        final Map<String, Object> map = ctx.getBeansWithAnnotation(ServerHandler.class);
                        LinkedList<Object> handlers = new LinkedList<Object>(map.values());
                        Iterator<Object> iterator = handlers.iterator();
                        while (iterator.hasNext()) {
                            Object o = iterator.next();
                            if (!(o instanceof ChannelHandlerAdapter && o.getClass().isAnnotationPresent(ServerHandler.class))) {
                                iterator.remove();
                            }
                        }
                        handlers.sort(new Comparator<Object>() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                int i = ((ServerHandler)(o1.getClass().getAnnotation(ServerHandler.class))).order();
                                int j = ((ServerHandler)(o2.getClass().getAnnotation(ServerHandler.class))).order();
                                return i-j;
                            }
                        });
                        handlers.addFirst(ctx.getBean(MessageDecoder.class));
                        handlers.addFirst(ctx.getBean(MessageEncoder.class));
                        for (Object h : handlers) ch.pipeline().addLast((ChannelHandlerAdapter)h);
                    }
                });

        try {
            ChannelFuture f = serverBootstrap.bind(Integer.parseInt(port)).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("server-spring.xml");
        ctx.getBean(Server.class).start();
    }
}
