package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import ch.inverseintegral.fakemc.handlers.MinecraftHandler;
import ch.inverseintegral.fakemc.handlers.PacketHandler;
import ch.inverseintegral.fakemc.handlers.Varint21FrameDecoder;
import ch.inverseintegral.fakemc.handlers.Varint21LengthFieldPrepender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.*;
import java.util.Base64;
import java.util.Properties;

/**
 * This class creates a new fake minecraft server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class FakeMC {

    public static void main(String[] args) {
        // Create a boss and a worker group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ConfigurationValues values = loadConfiguration();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // Initialize the channel pipeline
                            ch.pipeline().addLast(new Varint21FrameDecoder(),
                                    new Varint21LengthFieldPrepender(),
                                    new MinecraftHandler(),
                                    new PacketHandler(values));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind to the configured port and block until the server stops
            ChannelFuture f = b.bind(values.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static ConfigurationValues loadConfiguration() {
        ConfigurationValues values = new ConfigurationValues();

        // Load properties file
        try (InputStream inputStream = FakeMC.class.getClassLoader().getResourceAsStream("configuration.properties")){
            Properties properties = new Properties();
            properties.load(inputStream);

            values.setMotd((String) properties.get("motd"));
            values.setCurrentPlayers(Integer.valueOf((String) properties.get("online_players")));
            values.setMaxPlayers(Integer.valueOf((String) properties.get("max_players")));
            values.setKickMessage((String) properties.get("kick_message"));
            values.setPort(Integer.valueOf((String) properties.get("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load favicon file and encode as base64 string
        File iconFile = new File(FakeMC.class.getClassLoader()
                .getResource("favicon.png")
                .getFile());

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(iconFile))) {
            int length = (int) iconFile.length();
            byte[] bytes = new byte[length];

            reader.read(bytes, 0, length);
            reader.close();

            values.setFavicon(new String(Base64.getEncoder().encode(bytes)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values;
    }

}
