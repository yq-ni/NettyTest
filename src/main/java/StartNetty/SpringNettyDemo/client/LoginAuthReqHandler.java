package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/3/5.
 */

@ChannelHandler(order = 1)
public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(LoginAuthReqHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage nettyMessage = buildRequest();
        ctx.writeAndFlush(nettyMessage);
        LOGGER.info("send login request to "+ ctx.channel().remoteAddress().toString());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildRequest() {
        NettyMessage nettyMessage = new NettyMessage();
        nettyMessage.setType(MessageType.LOGIN_REQ.value);
        return nettyMessage;
    }


}
