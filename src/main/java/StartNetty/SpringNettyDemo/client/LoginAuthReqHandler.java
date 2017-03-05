package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ClientHandler;
import StartNetty.SpringNettyDemo.message.impl.Header;
import StartNetty.SpringNettyDemo.message.impl.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by nyq on 2017/3/5.
 */

@ClientHandler(order = 1)
public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage nettyMessage = buildRequest();
        ctx.writeAndFlush(nettyMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireChannelRead(cause);
    }

    private NettyMessage buildRequest() {
        NettyMessage nettyMessage = new NettyMessage();
        Header h = new Header();
        h.setType(1);
        nettyMessage.setHeader(h);
        return nettyMessage;
    }
}
