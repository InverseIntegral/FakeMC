package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import ch.inverseintegral.fakemc.handlers.MinecraftHandler;
import ch.inverseintegral.fakemc.handlers.PacketHandler;
import ch.inverseintegral.fakemc.handlers.Varint21FrameDecoder;
import ch.inverseintegral.fakemc.handlers.Varint21LengthFieldPrepender;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    @Autowired
    private ConfigurationValues configurationValues;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        System.out.println(configurationValues.getKickMessage());

        // Initialize the channel pipeline
        socketChannel.pipeline().addLast(new Varint21FrameDecoder(),
                new Varint21LengthFieldPrepender(),
                new MinecraftHandler(),
                new PacketHandler(configurationValues));

    }

}
