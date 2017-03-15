package StartNetty.SpringNettyDemo.message.codec.JavaSerializable;

import io.netty.handler.codec.serialization.ClassResolver;

import java.io.*;

/**
 * Created by nyq on 2017/3/13.
 */
class CompactObjectInputStream extends ObjectInputStream {

    private final ClassResolver classResolver;

    CompactObjectInputStream(InputStream in, ClassResolver classResolver) throws IOException {
        super(in);
        this.classResolver = classResolver;
    }

    @Override
    protected void readStreamHeader() throws IOException {
        int version = readByte() & 0xFF;
        if (version != STREAM_VERSION) {
            throw new StreamCorruptedException(
                    "Unsupported version: " + version);
        }
    }

    @Override
    protected ObjectStreamClass readClassDescriptor()
            throws IOException, ClassNotFoundException {
        int type = read();
        if (type < 0) {
            throw new EOFException();
        }
        switch (type) {
            case CompactObjectOutputStream.TYPE_FAT_DESCRIPTOR:
                return super.readClassDescriptor();
            case CompactObjectOutputStream.TYPE_THIN_DESCRIPTOR:
                String className = readUTF();
                Class<?> clazz = classResolver.resolve(className);
                return ObjectStreamClass.lookupAny(clazz);
            default:
                throw new StreamCorruptedException(
                        "Unexpected class descriptor type: " + type);
        }
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Class<?> clazz;
        try {
            clazz = classResolver.resolve(desc.getName());
        } catch (ClassNotFoundException ignored) {
            clazz = super.resolveClass(desc);
        }

        return clazz;
    }

}
