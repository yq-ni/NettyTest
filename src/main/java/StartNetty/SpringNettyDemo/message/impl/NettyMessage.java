package StartNetty.SpringNettyDemo.message.impl;

import StartNetty.SpringNettyDemo.message.AbstractMessage;
import StartNetty.SpringNettyDemo.message.impl.Header;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessage extends AbstractMessage{
    private Header header;
    private String body = "";

    public NettyMessage() {

    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return header.toString() + '\n' + body;
    }
}
