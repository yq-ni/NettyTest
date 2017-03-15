package StartNetty.SpringNettyDemo.message.codec.JavaSerializable;

import StartNetty.SpringNettyDemo.message.AbstractDecoder;
import StartNetty.SpringNettyDemo.message.struct.NettyMessage;
import com.esotericsoftware.kryo.Kryo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

import java.io.*;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessageDecoder implements AbstractDecoder<NettyMessage> {

    @Override
    public NettyMessage decode(byte[] bytes) throws Exception{
        ObjectInputStream is = new CompactObjectInputStream(new ByteBufInputStream(Unpooled.copiedBuffer(bytes)), ClassResolvers.weakCachingConcurrentResolver(NettyMessage.class.getClassLoader()));
        Object result = is.readObject();
        is.close();
        return (NettyMessage) result;

    }


}
