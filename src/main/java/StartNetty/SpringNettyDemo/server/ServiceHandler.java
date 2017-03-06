package StartNetty.SpringNettyDemo.server;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.AbstractMessage;
import StartNetty.SpringNettyDemo.message.impl.MessageType;
import StartNetty.SpringNettyDemo.message.impl.NettyMessage;
import StartNetty.SpringNettyDemo.service.ServicePool;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by nyq on 2017/3/6.
 */

@ChannelHandler
public class ServiceHandler extends ChannelHandlerAdapter {
    private static final ServicePool SERVICE_POOL = ServicePool.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (LoginAuthResHandler.nodeRecord.containsKey(ctx.channel().remoteAddress().toString())) {
            NettyMessage n = (NettyMessage) msg;
            if (n.getHeader() != null && n.getHeader().getType() == MessageType.SERVICE_REQ.value) {
                SERVICE_POOL.execute(n);
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }
}
