package StartNetty.SpringNettyDemo.message;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
public interface AbstractEncoder<T extends AbstractMessage> {
    void encode(final ByteBuf out, T message) throws Exception;
}
