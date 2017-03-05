package StartNetty.SpringNettyDemo.message.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Formatter;

/**
 * Created by nyq on 2017/3/5.
 */
public class Header {
    public static final int HEADER_LENGTH = 4 + 4 + 4;

    private int crcCode = 0x12340101;
    private int type;
    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "crcCode:" + crcCode + " type:" + type + " priority:" + priority ;
    }
}
