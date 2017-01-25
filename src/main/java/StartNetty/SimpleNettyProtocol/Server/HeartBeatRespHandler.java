package StartNetty.SimpleNettyProtocol.Server;

import StartNetty.SimpleNettyProtocol.Client.HeartBeatReqHandler;
import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter{
    private static final Logger LOGGER = Logger.getLogger(HeartBeatReqHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            LOGGER.info("Receive client heart beat message: " + message);
            NettyMessage heartBeat = buildHeatBeat();
            LOGGER.info("Send heart beat response message to client: " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
