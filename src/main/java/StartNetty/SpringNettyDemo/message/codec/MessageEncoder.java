package StartNetty.SpringNettyDemo.message.codec;

import StartNetty.SpringNettyDemo.config.ChannelHandler;
import StartNetty.SpringNettyDemo.message.AbstractEncoder;
import StartNetty.SpringNettyDemo.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
@ChannelHandler(order = 0)
public class MessageEncoder extends MessageToByteEncoder<AbstractMessage>{

    private AbstractEncoder abstractEncoder;

    @Autowired
    public MessageEncoder(AbstractEncoder abstractEncoder) {
        this.abstractEncoder = abstractEncoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, ByteBuf out) throws Exception {
        abstractEncoder.encode(out, msg);
    }

}

