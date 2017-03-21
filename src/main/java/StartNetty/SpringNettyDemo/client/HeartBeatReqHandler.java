package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.services.HelloService;
import StartNetty.SpringNettyDemo.services.Person;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ResourceLeakDetector;

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
        if (nettyMessage != null && nettyMessage.getType() == MessageType.LOGIN_RES.value) {
            ServiceHandler serviceHandler = ctx.channel().pipeline().get(ServiceHandler.class);
            if (!serviceHandler.isLogin()) {
                ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
                ctx.executor().execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (serviceHandler.getList()) {
                            serviceHandler.setLogin(true);
                        }
                        while (serviceHandler.getList().size() != 0) {
                            ctx.writeAndFlush(serviceHandler.getList().poll());
                        }
                    }
                });
                LOGGER.info("Login successfully! Start to send heartbeat...");
            }
        }
        else if (nettyMessage.getType() == MessageType.HEARTBEAT_RES.value) {

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
            n.setType(MessageType.HEARTBEAT_REQ.value);
            ctx.writeAndFlush(n);
        }
    }
}
