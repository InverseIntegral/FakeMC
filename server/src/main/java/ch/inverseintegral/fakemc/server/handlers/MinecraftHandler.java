package ch.inverseintegral.fakemc.server.handlers;

import ch.inverseintegral.fakemc.server.Protocol;
import ch.inverseintegral.fakemc.server.ServerStatistics;
import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Handles the encoding and decoding of minecraft packets.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class MinecraftHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    private static final Logger logger = LoggerFactory.getLogger(MinecraftHandler.class);

    /**
     * The current protocol that is used for encoding and decoding the packets.
     * @see #setProtocol(Protocol)
     */
    private Protocol protocol = Protocol.HANDSHAKE;

    /**
     * {@inheritDoc}
     * Reads the packet id and instantiates the found packet.
     * @see Protocol
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int packetId = Packet.readVarInt(in);

        Class<?> packetClass = protocol.getClassById(packetId);

        if (packetClass == null) {
            logger.warn("Unknown packet with id {} received", packetId);
            in.skipBytes(in.readableBytes());
            return;
        }

        Packet packet = (Packet) packetClass.newInstance();
        packet.read(in);

        out.add(packet);
    }

    /**
     * {@inheritDoc}
     * Writes the packet id to the byte representation.
     * @see Protocol
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        int packetId = protocol.getIdByClass(msg.getClass());

        ByteBuf buf = Unpooled.buffer();
        Packet.writeVarInt(packetId, buf);
        msg.write(buf);

        out.add(buf);
    }

    void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

}
