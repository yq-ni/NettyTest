package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.impl.Header;
import StartNetty.SpringNettyDemo.message.impl.MessageType;
import StartNetty.SpringNettyDemo.message.impl.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/3/5.
 */

@ChannelHandler(order = 2)
public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatReqHandler.class.getName());
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        Header header = nettyMessage.getHeader();
        if (header != null && header.getType() == MessageType.LOGIN_RES.value) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
            ctx.executor().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    NettyMessage n = new NettyMessage();
                    Header h = new Header();
                    h.setType(MessageType.SERVICE_REQ.value);
                    n.setBody("This is a service task.");
                    n.setHeader(h);
                    ctx.writeAndFlush(n);
                }
            }, 2, 3, TimeUnit.SECONDS);

            LOGGER.info("Login successfully! Start to send heartbeat...");
        }
        else if (header != null && header.getType() == MessageType.HEARTBEAT_RES.value) {

        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
            LOGGER.warning("Cancel to send heartbeat...");
        }
        ctx.fireExceptionCaught(cause);
    }

    private static class HeartBeatTask implements Runnable {
        private ChannelHandlerContext ctx;
        public HeartBeatTask(ChannelHandlerContext ctx) { this.ctx = ctx; }

        @Override
        public void run() {
            NettyMessage n = new NettyMessage();
            Header h = new Header();
            h.setType(MessageType.HEARTBEAT_REQ.value);
            n.setHeader(h);
            ctx.writeAndFlush(n);
        }
    }
}
