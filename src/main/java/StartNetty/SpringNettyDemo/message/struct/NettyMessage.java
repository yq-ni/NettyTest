package StartNetty.SpringNettyDemo.message.struct;

import StartNetty.SpringNettyDemo.message.AbstractMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nyq on 2017/3/5.
 */
public class NettyMessage extends AbstractMessage implements Serializable {
    private int crcCode = 0x12340101;
    private int type;
    private int priority;
    private Object attachment;

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

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
        return "crcCode:" + crcCode + " type:" + type + " priority:" + priority + " attachment: " + attachment;
    }
}
