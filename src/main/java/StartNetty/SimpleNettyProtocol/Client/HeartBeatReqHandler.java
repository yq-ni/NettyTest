package StartNetty.SimpleNettyProtocol.Client;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(HeartBeatReqHandler.class.getName());
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        Header header = nettyMessage.getHeader();

        if (header != null && header.getType() == MessageType.LOGIN_RESP.value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        } else if (header != null && header.getType() == MessageType.HEARTBEAT_RESP.value()) {
            LOGGER.info("Client receive server heart beat message: " + nettyMessage);
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
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext context) {
            ctx = context;
        }

        public void run() {
            NettyMessage nettyMessage = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            nettyMessage.setHeader(header);
            LOGGER.info("Client send heart beat message to server :" + nettyMessage);
            ctx.writeAndFlush(nettyMessage);
        }
    }
}
