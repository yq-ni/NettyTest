package StartNetty.SpringNettyDemo.message.impl;

/**
 * Created by nyq on 2017/3/5.
 */
public enum  MessageType {
    LOGIN_REQ(1), LOGIN_RES(2), HEARTBEAT_REQ(3), HEARTBEAT_RES(4),
    SERVICE_REQ(5), SERVICE_RES(6);

    public final int value;

    MessageType(int value) {
        this.value = value;
    }


}
