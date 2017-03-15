package StartNetty.SpringNettyDemo.message.codec;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.AbstractDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */

@ChannelHandler(order = 0)
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    private AbstractDecoder abstractDecoder;

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, AbstractDecoder abstractDecoder) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.abstractDecoder = abstractDecoder;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
//        if (frame == null) {
//            return null;
//        }
//        int len = frame.readInt();


//        remember to release the frame or it will result into memory leak

        if (in.readableBytes() < 4)
            return null;
        in.markReaderIndex();
        int len = in.readInt();
        if (len < 0) {
            ctx.close();
        }
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return null;
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return abstractDecoder.decode(bytes);
    }


}
