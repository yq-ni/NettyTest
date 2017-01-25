package StartNetty.SimpleNettyProtocol.Client;

import StartNetty.SimpleNettyProtocol.Configs;
import StartNetty.SimpleNettyProtocol.Message.Codec.MessageDecoder;
import StartNetty.SimpleNettyProtocol.Message.Codec.MessageEncoder;
import StartNetty.SimpleNettyProtocol.Message.Struct.Header;
import StartNetty.SimpleNettyProtocol.Message.Struct.MessageType;
import StartNetty.SimpleNettyProtocol.Message.Struct.NettyMessage;
import StartNetty.SimpleNettyProtocol.Server.ServiceRespHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    private ScheduledExecutorService executor = Executors
            .newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new MessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast("MessageEncoder",
                                    new MessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",
                                    new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler",
                                    new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatHandler",
                                    new HeartBeatReqHandler());
                            ch.pipeline().addLast("ServiceReqHandler", new ServiceReqHandler());
                        }
                    });

            final ChannelFuture future = b.connect(
                    new InetSocketAddress(host, port),
                    new InetSocketAddress(Configs.LOCAL_IP,
                            Configs.LOCAL_PORT)).sync();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000L);
                    }
                    catch (Exception e) {
                        return;
                    }
                    NettyMessage message = new NettyMessage();
                    Header header = new Header();
                    header.setType(MessageType.SERVICE_REQ.value());
                    message.setHeader(header);
                    message.setBody("This is the body");
                    LOG.info("Client send service message: " + message);
                    future.channel().writeAndFlush(message);
                }
            }).start();


            future.channel().closeFuture().sync();
        } finally {
            //reconnect
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        try {
                            connect(Configs.REMOTE_PORT, Configs.REMOTE_IP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(Configs.REMOTE_PORT, Configs.REMOTE_IP);
    }
}
