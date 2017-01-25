package StartNetty.SimpleNettyProtocol.Message.Struct;

/**
 * Created by nyq on 2017/1/25.
 */
public final class NettyMessage {
    private Header header;
    private Object body;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage [header=" + header + ", body=" + body + "]";
    }
}