package StartNetty.SimpleNettyProtocol.Client;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class ServiceReqHandler extends ChannelHandlerAdapter{
    private static final Logger LOGGER = Logger.getLogger(ServiceReqHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        Header header = message.getHeader();
        if (header != null && header.getType() == MessageType.SERVICE_RESP.value()) {
            LOGGER.info("Client receive executor resp message: " + message);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}
