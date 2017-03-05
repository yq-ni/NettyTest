package StartNetty.SpringNettyDemo.client;

import StartNetty.SpringNettyDemo.config.ClientHandler;
import io.netty.channel.ChannelHandlerAdapter;

/**
 * Created by nyq on 2017/3/5.
 */

@ClientHandler(order = 2)
public class HeartBeatReqHandler extends ChannelHandlerAdapter {


}
