package ch.inverseintegral.fakemc.handlers;

import ch.inverseintegral.fakemc.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Prepends the length of the packet to the encoded message.
 *
 * @author md-5
 * @version 1.0
 * @since 1.0
 */
@ChannelHandler.Sharable
public class Varint21LengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        // Length of the body
        int bodyLen = msg.readableBytes();
        int headerLen = varintSize(bodyLen);

        // Ensure the capacity in the buffer
        out.ensureWritable(headerLen + bodyLen);

        // Write the header and body
        Packet.writeVarInt(bodyLen, out);
        out.writeBytes(msg);
    }

    /**
     * Gets the amount of bytes for this varint representation.
     *
     * @param paramInt The int that will be written as warint
     * @return Returns the amount of bytes for this varint
     * This number is between 1 and 5
     */
    private static int varintSize(int paramInt) {
        if ((paramInt & 0xFFFFFF80) == 0) {
            return 1;
        }

        if ((paramInt & 0xFFFFC000) == 0) {
            return 2;
        }

        if ((paramInt & 0xFFE00000) == 0) {
            return 3;
        }

        if ((paramInt & 0xF0000000) == 0) {
            return 4;
        }

        return 5;
    }
}