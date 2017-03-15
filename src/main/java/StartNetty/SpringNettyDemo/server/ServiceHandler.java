package StartNetty.SpringNettyDemo.server;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.struct.MessageType;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import StartNetty.SpringNettyDemo.message.struct.Request;
import StartNetty.SpringNettyDemo.message.struct.Response;
import StartNetty.SpringNettyDemo.server.executor.OnTaskComplete;
import StartNetty.SpringNettyDemo.server.executor.ServicePool;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by nyq on 2017/3/6.
 */

@ChannelHandler(order = 3)
public class ServiceHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (LoginAuthResHandler.nodeRecord.containsKey(ctx.channel().remoteAddress().toString())) {
            NettyMessage n = (NettyMessage) msg;
            if (n.getType() == MessageType.SERVICE_REQ.value) {
                final NettyMessage serviceRes = new NettyMessage();
                serviceRes.setType(MessageType.SERVICE_RES.value);
                final Response response = new Response();
                response.setID(((Request)n.getAttachment()).getID());
                serviceRes.setAttachment(response);
                ServicePool.getInstance().execute(n, new OnTaskComplete() {
                    @Override
                    public void success(Object result) {
                        response.setResult(result);
                        ctx.writeAndFlush(serviceRes);
                    }

                    @Override
                    public void fail(Object error) {
                        response.setError(error.toString());
                        ctx.writeAndFlush(serviceRes);
                    }
                });
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }


}
