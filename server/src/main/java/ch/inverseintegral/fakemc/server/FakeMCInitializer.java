package ch.inverseintegral.fakemc.server;

import ch.inverseintegral.fakemc.server.config.ConfigurationValues;
import ch.inverseintegral.fakemc.server.handlers.MinecraftHandler;
import ch.inverseintegral.fakemc.server.handlers.PacketHandler;
import ch.inverseintegral.fakemc.server.handlers.Varint21FrameDecoder;
import ch.inverseintegral.fakemc.server.handlers.Varint21LengthFieldPrepender;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initializes the channel pipeline by adding channel handlers to it.
 * Most handlers are not {@link io.netty.channel.ChannelHandler.Sharable sharable}
 * therefore they are not {@link Autowired autowired}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
class FakeMCInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private Varint21LengthFieldPrepender fieldPrepender;

    @Autowired
    private ConfigurationValues configurationValues;

    @Autowired
    private String favicon;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new Varint21FrameDecoder(),
                fieldPrepender,
                new MinecraftHandler(),
                new PacketHandler(configurationValues, favicon));

    }

}
