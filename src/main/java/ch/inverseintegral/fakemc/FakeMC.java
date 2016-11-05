package ch.inverseintegral.fakemc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * This class is the entry point of the application.
 * It does configure some netty objects and invokes the server
 * {@link FakeMCServer#start() start}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class FakeMC {

    @Autowired
    private ch.inverseintegral.fakemc.FakeMCInitializer channelInitializer;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(FakeMC.class, args);
        context.getBean(FakeMCServer.class).start();
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
