package ch.inverseintegral.fakemc.handlers;

import ch.inverseintegral.fakemc.Protocol;
import ch.inverseintegral.fakemc.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * Handles the encoding and decoding of minecraft packets.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class MinecraftHandler extends MessageToMessageCodec<ByteBuf, Packet> {

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
            System.out.println(packetId + " is an unknown packet");
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

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

}
