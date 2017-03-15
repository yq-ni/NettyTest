package StartNetty.SpringNettyDemo.message.codec.JavaSerializable;


import StartNetty.SpringNettyDemo.message.AbstractEncoder;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.*;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessageEncoder implements AbstractEncoder<NettyMessage> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    public void encode(ByteBuf out, NettyMessage message) throws Exception {
        if (message == null) {
            return;
        }
        int startIndex = out.writerIndex();
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        byteBufOutputStream.write(LENGTH_PLACEHOLDER);
        ObjectOutputStream objectOutputStream = new CompactObjectOutputStream(byteBufOutputStream);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
        objectOutputStream.close();

        int endIndex = out.writerIndex();
        out.setInt(startIndex, endIndex - startIndex - 4);
    }



}
