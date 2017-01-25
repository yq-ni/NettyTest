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
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(LoginAuthReqHandler.class.getName());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        Header header = nettyMessage.getHeader();
        if (header != null && header.getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (Byte) nettyMessage.getBody();
            if (loginResult != (byte)0) {
                ctx.close();
            }
            else {
                LOGGER.info("Login :" + nettyMessage);
                ctx.fireChannelRead(msg);
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }
}
