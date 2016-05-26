package ch.inverseintegral.fakemc.handlers;

import ch.inverseintegral.fakemc.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Reads the length of the packet from the byte buffer.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class Varint21FrameDecoder extends ByteToMessageDecoder {

    private static boolean DIRECT_WARNING;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Mark the current reader index
        in.markReaderIndex();

        final byte[] buf = new byte[3];

        for (int i = 0; i < buf.length; i++) {

            // Check if there is no content to read
            if (!in.isReadable()) {

                // Reset the index to its former place
                in.resetReaderIndex();
                return;
            }

            buf[i] = in.readByte();

            if (buf[i] >= 0) {
                int length = Packet.readVarInt(Unpooled.wrappedBuffer(buf));

                // Check the length field
                if (length == 0) {
                    throw new CorruptedFrameException("Empty Packet!");
                }

                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                    return;
                } else {

                    // Has a reference to the low level memory address
                    if (in.hasMemoryAddress()) {
                        out.add(in.slice(in.readerIndex(), length).retain());
                        in.skipBytes(length);
                    } else {
                        if (!DIRECT_WARNING) {
                            DIRECT_WARNING = true;
                            System.out.println("Netty is not using direct IO buffers.");
                        }

                        // See https://github.com/SpigotMC/BungeeCord/issues/1717
                        ByteBuf dst = ctx.alloc().directBuffer(length);
                        in.readBytes(dst);
                        out.add(dst);
                    }

                    return;
                }
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}