package StartNetty.SpringNettyDemo.client;


import StartNetty.SpringNettyDemo.config.ClientHandler;
import StartNetty.SpringNettyDemo.message.codec.MessageDecoder;
import StartNetty.SpringNettyDemo.message.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by nyq on 2017/3/5.
 */

@Component
public class Client {

    private ApplicationContext ctx;

    @Value("${server.host}")
    private String HOST;

    @Value("${server.port}")
    private  int PORT;

    @Autowired
    public Client(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public void start() {

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        final Map<String, Object> map = ctx.getBeansWithAnnotation(ClientHandler.class);
                        LinkedList<Object> handlers = new LinkedList<Object>(map.values());
                        Iterator<Object> iterator = handlers.iterator();
                        while (iterator.hasNext()) {
                            Object o = iterator.next();
                            if (!(o instanceof ChannelHandlerAdapter && o.getClass().isAnnotationPresent(ClientHandler.class))) {
                                iterator.remove();
                            }
                        }
                        handlers.sort(new Comparator<Object>() {
                            @Override
                            public int compare(Object o1, Object o2) {
                                int i = ((ClientHandler)(o1.getClass().getAnnotation(ClientHandler.class))).order();
                                int j = ((ClientHandler)(o2.getClass().getAnnotation(ClientHandler.class))).order();
                                return i-j;
                            }
                        });
                        handlers.addFirst(ctx.getBean(MessageDecoder.class));
                        handlers.addFirst(ctx.getBean(MessageEncoder.class));
                        for (Object h : handlers) ch.pipeline().addLast((ChannelHandlerAdapter)h);
                    }
                });
        try {
            ChannelFuture f = bootstrap.connect(new InetSocketAddress(HOST, PORT)).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("client-spring.xml");
        ctx.getBean(Client.class).start();
    }

}
