package ch.inverseintegral.fakemc.server.handlers;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Reads the length of the packet from the byte buffer.
 *
 * @author md-5
 * @version 1.0
 * @since 1.0
 */
public class Varint21FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        final byte[] buf = new byte[3];

        for (int i = 0; i < buf.length; i++) {

            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }

            buf[i] = in.readByte();

            if (buf[i] >= 0) {
                int length = Packet.readVarInt(Unpooled.wrappedBuffer(buf));

                if (length == 0) {
                    throw new CorruptedFrameException("Empty Packet!");
                }

                if (in.readableBytes() < length) {
                    in.resetReaderIndex();
                    return;
                } else {

                    if (in.hasMemoryAddress()) {
                        out.add(in.slice(in.readerIndex(), length).retain());
                        in.skipBytes(length);
                    } else {
                        ByteBuf dst = ctx.alloc().directBuffer(length);
                        in.readBytes(dst);
                        out.add(dst);
                    }

                    return;
                }
            }
        }

        throw new CorruptedFrameException("Length is wider than 21 bits");
    }
}