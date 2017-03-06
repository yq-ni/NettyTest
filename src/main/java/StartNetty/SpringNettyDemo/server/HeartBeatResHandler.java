package StartNetty.SpringNettyDemo.server;

import StartNetty.SimpleNettyProtocol.Client.HeartBeatReqHandler;
import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.config.SharableChannelHandler;
import StartNetty.SpringNettyDemo.message.impl.Header;
import StartNetty.SpringNettyDemo.message.impl.MessageType;
import StartNetty.SpringNettyDemo.message.impl.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by nyq on 2017/3/6.
 */

@ChannelHandler(order = 2)
public class HeartBeatResHandler extends ChannelHandlerAdapter {
    private final static Logger LOGGER = Logger.getLogger(HeartBeatReqHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (! LoginAuthResHandler.nodeRecord.containsKey(ctx.channel().remoteAddress().toString())) {
            ctx.fireChannelRead(msg);
        }
        else {
            NettyMessage n = new NettyMessage();
            Header h = new Header();
            h.setType(MessageType.HEARTBEAT_RES.value);
            ctx.writeAndFlush(n);
        }
    }
}
