package StartNetty.SimpleNettyProtocol.Message.Codec;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

/**
 * Created by nyq on 2017/1/25.
 */
public class MessageEncoder extends MessageToByteEncoder<NettyMessage> {
    private MarshallingEncoder marshallingEncoder;

    public MessageEncoder() throws IOException {
        marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("nettyMessage is invalid");
        }

        Header header = msg.getHeader();

        out.writeInt(header.getCrcCode());
        out.writeInt(header.getLength());
        out.writeLong(header.getSessionID());
        out.writeByte(header.getType());
        out.writeByte(header.getPriority());
        out.writeInt(header.getAttachment().size());
        for (Map.Entry<String, Object> entry : header.getAttachment().entrySet()) {
            byte[] keyByte = entry.getKey().getBytes("UTF-8");
            out.writeInt(keyByte.length);
            out.writeBytes(keyByte);
            Object value = entry.getValue();
            marshallingEncoder.encode(value, out);
        }

        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), out);
        }
        else {
            out.writeInt(0);
        }
        out.setInt(4, out.readableBytes() - 8);
    }
}
