package StartNetty.SpringNettyDemo.message.codec;

import StartNetty.SpringNettyDemo.message.AbstractDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    private AbstractDecoder abstractDecoder;

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, AbstractDecoder abstractDecoder) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.abstractDecoder = abstractDecoder;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        frame.readInt();
        ByteBuffer byteBuffer = frame.nioBuffer();
        return abstractDecoder.decode(byteBuffer);
    }


}
