package StartNetty.SpringNettyDemo.message.codec.Kryo;

import StartNetty.SpringNettyDemo.message.AbstractDecoder;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

/**
 * Created by nyq on 2017/3/15.
 */
public class KryoDecoder implements AbstractDecoder<NettyMessage> {
    @Override
    public NettyMessage decode(byte[] bytes) throws Exception {
        Input input = new Input(bytes);
        Kryo kryo = new Kryo();
        NettyMessage n = kryo.readObject(input, NettyMessage.class);
        input.close();
        return n;
    }
}
