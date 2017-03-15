package StartNetty.SpringNettyDemo.server;


import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/3/5.
 */

@ChannelHandler(order = 1)
public class LoginAuthResHandler extends ChannelHandlerAdapter{
    public static ConcurrentHashMap<String, Boolean> nodeRecord = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(LoginAuthResHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage n = (NettyMessage) msg;
        if (n.getType() == MessageType.LOGIN_REQ.value) {
            // TODO: 2017/3/6 check the request
            String address = ctx.channel().remoteAddress().toString();
            if (nodeRecord.get(address) != null) {
                buildResponse(ctx, -1);
            }
            else {
                buildResponse(ctx, MessageType.LOGIN_RES.value);
                nodeRecord.put(address, true);
                LOGGER.info(address + " login...");
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeRecord.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(ChannelHandlerContext ctx, int type) {
        NettyMessage nettyMessage = new NettyMessage();
        nettyMessage.setType(type);
        ctx.writeAndFlush(nettyMessage);
        return nettyMessage;
    }
}
