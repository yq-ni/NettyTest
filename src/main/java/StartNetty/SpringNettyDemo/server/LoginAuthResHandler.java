package StartNetty.SpringNettyDemo.server;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import StartNetty.SpringNettyDemo.config.ServerHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by nyq on 2017/3/5.
 */

@ServerHandler(order = 1)
public class LoginAuthResHandler extends ChannelHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.print(msg);
    }

    private NettyMessage buildResponse() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        return nettyMessage;
    }
}
