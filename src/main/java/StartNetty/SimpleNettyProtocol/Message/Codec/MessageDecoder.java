package StartNetty.SimpleNettyProtocol.Message.Codec;

import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nyq on 2017/1/25.
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder{
    MarshallingDecoder marshallingDecoder;

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attachment = new HashMap<String, Object>();
            for (int i = 0; i < size; i++) {
                int keySize = frame.readInt();
                byte[] keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                String key = new String(keyArray, "UTF-8");
                attachment.put(key, marshallingDecoder.decode(frame));
            }
            header.setAttachment(attachment);
        }
        if (frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }
        message.setHeader(header);
        return message;
    }
}
