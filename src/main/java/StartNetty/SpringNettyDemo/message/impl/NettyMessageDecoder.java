package StartNetty.SpringNettyDemo.message.impl;

import StartNetty.SpringNettyDemo.message.AbstractDecoder;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessageDecoder implements AbstractDecoder<NettyMessage> {

    @Override
    public NettyMessage decode(ByteBuffer byteBuffer) throws Exception{
        if (byteBuffer == null || byteBuffer.capacity() < Header.HEADER_LENGTH) {
            throw new Exception("invalid input");
        }
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(byteBuffer.getInt());
        header.setType(byteBuffer.getInt());
        header.setPriority(byteBuffer.getInt());
        message.setHeader(header);
        if (byteBuffer.hasRemaining()) {
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            message.setBody(new String(bytes));
        }
        return message;
    }
}
