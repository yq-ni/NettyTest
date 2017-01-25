package StartNetty.SimpleNettyProtocol.Server;

import StartNetty.SimpleNettyProtocol.Configs;
import StartNetty.SimpleNettyProtocol.Message.Codec.MessageDecoder;
import StartNetty.SimpleNettyProtocol.Message.Codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by nyq on 2017/1/25.
 */
public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch)
                            throws IOException {
                        ch.pipeline().addLast(
                                new MessageDecoder(1024 * 1024, 4, 4));
                        ch.pipeline().addLast(new MessageEncoder());
                        ch.pipeline().addLast("readTimeoutHandler",
                                new ReadTimeoutHandler(50));
                        ch.pipeline().addLast(new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatHandler",
                                new HeartBeatRespHandler());
                        ch.pipeline().addLast("ServiceRespHandler", new ServiceRespHandler());
                    }
                });

        ChannelFuture f = b.bind(Configs.REMOTE_IP, Configs.REMOTE_PORT).sync();
        LOG.info("Netty server start ok : "
                + (Configs.REMOTE_IP + " : " + Configs.REMOTE_PORT));
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws Exception {
        new Server().bind();
    }
}
