package ch.inverseintegral.fakemc.server;

import ch.inverseintegral.fakemc.server.config.ConfigurationValues;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
public class FakeMCServer {

    @Autowired
    private ChannelInitializer<SocketChannel> channelInitializer;

    @Autowired
    private ConfigurationValues configurationValues;

    /**
     * Starts the netty server on the given port.
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        serverBootstrap().bind(configurationValues.getPort())
                .sync()
                .channel()
                .closeFuture()
                .sync();
    }

    @Bean
    public ServerBootstrap serverBootstrap() {
        EventLoopGroup bossGroup = bossGroup();
        EventLoopGroup workerGroup = workerGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        return serverBootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

}
