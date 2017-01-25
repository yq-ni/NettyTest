package StartNetty.SimpleNettyProtocol.Message.Codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * Created by nyq on 2017/1/25.
 */
public class ChannelBufferByteOutput implements ByteOutput {

    private final ByteBuf buffer;

    public ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    public void write(byte[] b) throws IOException {
        buffer.writeBytes(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        buffer.writeBytes(b, off, len);
    }

    public void close() throws IOException {

    }

    public void flush() throws IOException {

    }
}
