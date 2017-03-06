package StartNetty.SpringNettyDemo.config;

import StartNetty.SpringNettyDemo.message.codec.MessageDecoder;
import StartNetty.SpringNettyDemo.message.codec.MessageEncoder;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by nyq on 2017/3/6.
 */
public class BaseChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ApplicationContext ctx;

    public BaseChannelInitializer(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Map<String, Object> map = ctx.getBeansWithAnnotation(ChannelHandler.class);
        map.putAll(ctx.getBeansWithAnnotation(SharableChannelHandler.class));
        LinkedList<Object> handlers = new LinkedList<Object>(map.values());
        Iterator<Object> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (!(o instanceof ChannelHandlerAdapter &&
                    (o.getClass().isAnnotationPresent(ChannelHandler.class)
                            || o.getClass().isAnnotationPresent(SharableChannelHandler.class))) ){
                iterator.remove();
            }
        }
        handlers.sort(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                int i = o1.getClass().isAnnotationPresent(ChannelHandler.class) ?
                        ((ChannelHandler)(o1.getClass().getAnnotation(ChannelHandler.class))).order()
                        : ((SharableChannelHandler)(o1.getClass().getAnnotation(SharableChannelHandler.class))).order();
                int j = o2.getClass().isAnnotationPresent(ChannelHandler.class) ?
                        ((ChannelHandler)(o2.getClass().getAnnotation(ChannelHandler.class))).order()
                        :((SharableChannelHandler)(o2.getClass().getAnnotation(SharableChannelHandler.class))).order();
                return i-j;
            }
        });
        handlers.addFirst(ctx.getBean(MessageDecoder.class));
        handlers.addFirst(ctx.getBean(MessageEncoder.class));
        for (Object h : handlers) ch.pipeline().addLast((ChannelHandlerAdapter)h);
    }
}
