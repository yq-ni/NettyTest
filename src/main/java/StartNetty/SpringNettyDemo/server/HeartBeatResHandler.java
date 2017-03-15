package StartNetty.SpringNettyDemo.server;

import StartNetty.SimpleNettyProtocol.Client.HeartBeatReqHandler;
import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
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
        if (! LoginAuthResHandler.nodeRecord.containsKey(ctx.channel().remoteAddress().toString())
                || ((NettyMessage) msg).getType() != MessageType.HEARTBEAT_REQ.value) {
            ctx.fireChannelRead(msg);
        }
        else {
            NettyMessage n = new NettyMessage();
            n.setType(MessageType.HEARTBEAT_RES.value);
            ctx.writeAndFlush(n);
        }
    }
}
