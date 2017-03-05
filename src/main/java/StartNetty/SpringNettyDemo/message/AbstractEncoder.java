package StartNetty.SpringNettyDemo.message;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
public interface AbstractEncoder<T extends AbstractMessage> {
    ByteBuffer encode(T message) throws Exception;
}
