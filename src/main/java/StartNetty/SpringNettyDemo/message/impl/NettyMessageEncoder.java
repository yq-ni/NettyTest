package StartNetty.SpringNettyDemo.message.impl;


import StartNetty.SpringNettyDemo.message.AbstractEncoder;

import java.nio.ByteBuffer;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessageEncoder implements AbstractEncoder<NettyMessage> {
    @Override
    public ByteBuffer encode(NettyMessage message) throws Exception {
        if (message == null || message.getHeader() == null) {
            throw new Exception("invalid msg");
        }
        Header header = message.getHeader();
        int bodyLength = message.getBody() == null ? 0 : message.getBody().length() ;
        byte[] bytes = new byte[header.HEADER_LENGTH + bodyLength];
        intToBytes(header.getCrcCode(), bytes, 0);
        intToBytes(header.getType(), bytes, 4);
        intToBytes(header.getPriority(), bytes, 8);
        if (bodyLength != 0) {
            System.arraycopy(message.getBody().getBytes(), 0, bytes, 12, bodyLength);
        }
        return ByteBuffer.wrap(bytes);
    }

    private static void intToBytes(int n, byte[] bytes, int pos) {
        bytes[pos+3] = (byte) (n & 0xFF);
        bytes[pos+2] = (byte) ((n >> 8) & 0xFF);
        bytes[pos+1] = (byte) ((n >> 16) & 0xFF);
        bytes[pos] = (byte) ((n >> 24) & 0xFF);
    }
}
