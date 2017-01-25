package StartNetty.SimpleNettyProtocol.Server;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class ServiceRespHandler extends ChannelHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServiceRespHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        Header header = message.getHeader();
        if (header != null && header.getType() == MessageType.SERVICE_REQ.value()) {
            LOGGER.info("Receive client service req message: " + message);
            NettyMessage resp_msg = buildServiceResp();
            LOGGER.info("Send service resp to client: " + resp_msg);
            ctx.writeAndFlush(resp_msg);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildServiceResp() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.SERVICE_RESP.value());
        message.setHeader(header);
        return message;
    }
}
