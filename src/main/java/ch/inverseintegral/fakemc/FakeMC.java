package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.handlers.MinecraftHandler;
import ch.inverseintegral.fakemc.handlers.PacketHandler;
import ch.inverseintegral.fakemc.handlers.Varint21FrameDecoder;
import ch.inverseintegral.fakemc.handlers.Varint21LengthFieldPrepender;
import ch.inverseintegral.fakemc.ping.Chat;
import ch.inverseintegral.fakemc.ping.Players;
import ch.inverseintegral.fakemc.ping.Version;
import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;
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

        String configuration = loadConfiguration();

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
                                    new PacketHandler(configuration));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind to the port 25565 and block until the server stops
            ChannelFuture f = b.bind(25565).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static String loadConfiguration() {
        Gson gson = new Gson();
        Version version = new Version("1.8", 47);

        Players players = null;
        Chat chat = null;
        String favicon = null;

        // Load properties file
        try (InputStream inputStream = FakeMC.class.getClassLoader().getResourceAsStream("configuration.properties")){
            Properties properties = new Properties();
            properties.load(inputStream);

            String motd = (String) properties.get("motd");
            int currentPlayers = Integer.parseInt((String) properties.get("online_players"));
            int maxPlayer = Integer.parseInt((String) properties.get("max_players"));

            players = new Players(maxPlayer, currentPlayers, Collections.emptyList());
            chat = new Chat(motd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load favicon file and encode as base64 string
        File iconFile = new File(FakeMC.class.getClassLoader().getResource("favicon.png").getFile());

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(iconFile))) {
            int length = (int) iconFile.length();
            byte[] bytes = new byte[length];

            reader.read(bytes, 0, length);
            reader.close();

            favicon = new String(Base64.getEncoder().encode(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.toJson(new ch.inverseintegral.fakemc.ping.StatusResponse(chat, players, version, favicon));
    }

}
