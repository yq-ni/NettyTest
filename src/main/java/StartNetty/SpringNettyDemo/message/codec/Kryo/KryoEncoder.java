package StartNetty.SpringNettyDemo.message.codec.Kryo;

import StartNetty.SpringNettyDemo.message.AbstractEncoder;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

/**
 * Created by nyq on 2017/3/15.
 */
public class KryoEncoder implements AbstractEncoder<NettyMessage> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    public void encode(ByteBuf out, NettyMessage message) throws Exception {
        if (message == null) {
            return;
        }
        int startIndex = out.writerIndex();
        out.writeBytes(LENGTH_PLACEHOLDER);
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        Output output = new Output(byteBufOutputStream);
        Kryo kryo = new Kryo();
        kryo.writeObject(output, message);
        output.flush();
        output.close();

        out.setInt(startIndex, out.writerIndex() - startIndex -4);

    }
}
