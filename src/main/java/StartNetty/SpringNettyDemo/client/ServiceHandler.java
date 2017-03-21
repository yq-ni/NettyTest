package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.message.struct.Request;
import StartNetty.SpringNettyDemo.message.struct.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import sun.misc.Unsafe;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nyq on 2017/3/13.
 */

@ChannelHandler(order = 3)
public class ServiceHandler extends ChannelHandlerAdapter{

    private Channel channel;
    private final ConcurrentHashMap<String, ResFuture> map = new ConcurrentHashMap<>();
    private volatile boolean isLogin = false;
    private final LinkedList<NettyMessage> list = new LinkedList<>(); // just synchronized for add but not poll

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg != null && msg instanceof NettyMessage) {
            NettyMessage nettyMessage = (NettyMessage) msg;
            if (nettyMessage.getType() == MessageType.SERVICE_RES.value && nettyMessage.getAttachment() != null) {
                Response response = (Response) nettyMessage.getAttachment();
                if (map.containsKey(response.getID())) {
                    map.remove(response.getID()).done(response.getResult());
                }
            }
        }

        ctx.fireChannelRead(msg);
    }

    public ResFuture sendMessage(NettyMessage nettyMessage) {
        if (!isLogin) {
            synchronized (list) {
                if (!isLogin)
                    list.add(nettyMessage);
            }
        }
        else {
            channel.writeAndFlush(nettyMessage);
        }
        Request request = (Request)nettyMessage.getAttachment();
        ResFuture future = new ResFuture();
        map.put(request.getID(), future);
        return future;
    }

    public boolean isLogin() { return isLogin; }
    public void setLogin(boolean isLogin) { this.isLogin = isLogin; }
    public LinkedList<NettyMessage> getList() { return list; }

}
