package StartNetty.SpringNettyDemo.client;


import StartNetty.SpringNettyDemo.config.BaseInitializer;
import StartNetty.SpringNettyDemo.config.ClientHandler;
import StartNetty.SpringNettyDemo.message.codec.MessageDecoder;
import StartNetty.SpringNettyDemo.message.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Created by nyq on 2017/3/5.
 */
public class Client implements ApplicationContextAware, InitializingBean{

    private ApplicationContext ctx;

    private final String HOST;
    private final int PORT;

    public Client(String host, int port) {
        HOST = host;
        PORT = port;
    }

    public void start() {
        final Map<String, Object> map = ctx.getBeansWithAnnotation(ClientHandler.class);
        LinkedList<Object> handlers = new LinkedList<Object>(map.values());
        handlers.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((ClientHandler)o1).order() - ((ClientHandler)o2).order();
            }
        });
        handlers.addFirst(ctx.getBean(MessageDecoder.class));
        handlers.addFirst(ctx.getBean(MessageEncoder.class));

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new BaseInitializer(handlers));
        try {
            ChannelFuture f = bootstrap.connect(new InetSocketAddress(HOST, PORT)).sync();
            f.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
