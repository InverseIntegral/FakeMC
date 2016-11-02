package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import ch.inverseintegral.fakemc.handlers.MinecraftHandler;
import ch.inverseintegral.fakemc.handlers.PacketHandler;
import ch.inverseintegral.fakemc.handlers.Varint21FrameDecoder;
import ch.inverseintegral.fakemc.handlers.Varint21LengthFieldPrepender;
import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Base64;

/**
 * This class creates a new fake minecraft server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class FakeMC {

    private static final Logger logger = LoggerFactory.getLogger(FakeMC.class);

    public static void main(String[] args) {
        // Create a boss and a worker group
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ConfigurationValues values = loadConfiguration();
            String favicon = loadFavicon();
            logger.info("Configuration has been loaded");

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
                                    new PacketHandler(favicon, values));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind to the configured port and block until the server stops
            ChannelFuture f = b.bind(values.getPort()).sync();
            logger.info("Server has been started");

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Caught exception whilst waiting for server to bind to port", e);
        } catch (FileNotFoundException e) {
            logger.error("Unable to locate a configuration file", e);
        } catch (IOException e) {
            logger.error("IO Exception whilst loading configurations", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static ConfigurationValues loadConfiguration() throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileInputStream inputStream = new FileInputStream(getResourceFile("configuration.json"))){
            int value;

            while ((value = inputStream.read()) != -1) {
                content.append((char) value);
            }
        }

        Gson gson = new Gson();
        return gson.fromJson(content.toString(), ConfigurationValues.class);
    }

    private static String loadFavicon() throws IOException {
        File iconFile = new File(getResourceFile("favicon.png"));

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(iconFile))) {
            int length = (int) iconFile.length();
            byte[] bytes = new byte[length];

            reader.read(bytes, 0, length);
            reader.close();

            return new String(Base64.getEncoder().encode(bytes));
        }
    }

    private static String getResourceFile(String resource) throws FileNotFoundException {
        ClassLoader classLoader = FakeMC.class.getClassLoader();
        URL resourceURL = classLoader.getResource(resource);

        if (resourceURL == null) {
            throw new FileNotFoundException(resource + " file not found");
        }

        return resourceURL.getFile();
    }

}
