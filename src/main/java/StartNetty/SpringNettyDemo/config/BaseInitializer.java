package StartNetty.SpringNettyDemo.config;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

/**
 * Created by nyq on 2017/3/5.
 */
public class BaseInitializer extends ChannelInitializer<SocketChannel> {
    private  List<Object> list;

    public BaseInitializer(List<Object> list) {
        this.list = list;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        for (Object o : list) {
            if (o instanceof ChannelHandlerAdapter) {
                ch.pipeline().addLast((ChannelHandlerAdapter)o);
            }
        }
    }
}
