package StartNetty.SpringNettyDemo.message;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
public interface AbstractDecoder<T extends AbstractMessage> {
    T decode(ByteBuffer byteBuffer) throws Exception;
}
