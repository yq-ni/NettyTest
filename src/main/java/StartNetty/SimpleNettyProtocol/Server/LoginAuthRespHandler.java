package StartNetty.SimpleNettyProtocol.Server;

import StartNetty.SimpleNettyProtocol.Configs;
import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter{

    private static final Logger LOGGER = Logger.getLogger(LoginAuthRespHandler.class.getName());
    private Map<String, Boolean> nodeRecord = new ConcurrentHashMap<String, Boolean>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        Header header = message.getHeader();
        if (header != null && header.getType() == MessageType.LOGIN_REQ.value()) {
            String remoteAddr = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            if (nodeRecord.containsKey(remoteAddr)) {
                loginResp = buildResponse((byte)-1);
            }
            else {
                String ip = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
                boolean isWhite = checkWhite(ip);
                if (isWhite) {
                    nodeRecord.put(ip, true);
                }
                loginResp = isWhite ? buildResponse((byte)0) : buildResponse((byte)-1);
            }
            LOGGER.info("The login response is : " + loginResp);
            ctx.writeAndFlush(loginResp);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeRecord.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    private boolean checkWhite(String ip) {
        for (String IP : Configs.WHITE_LIST) {
            if (IP.equals(ip)) {
                return true;
            }
        }
        return false;
    }
}
