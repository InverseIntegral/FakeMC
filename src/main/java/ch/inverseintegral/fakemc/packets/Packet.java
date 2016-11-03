package ch.inverseintegral.fakemc.packets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * An abstract super class for packets.
 *
 * @author md-5
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public abstract class Packet {

    /**
     * Defines how the packet is read from the ByteBuf.
     *
     * @param buf The byte buf to read from
     */
    public abstract void read(ByteBuf buf);

    /**
     * Defines how the packet is written to a ByteBuf.
     *
     * @param buf The byte buf to write to.
     */
    public abstract void write(ByteBuf buf);

    protected static void writeString(String s, ByteBuf buf) {
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(b.length, buf);
        buf.writeBytes(b);
    }

    protected static String readString(ByteBuf buf) {
        int len = readVarInt(buf);

        byte[] b = new byte[len];
        buf.readBytes(b);

        return new String(b, StandardCharsets.UTF_8);
    }

    public static int readVarInt(ByteBuf input) {
        return readVarInt(input, 5);
    }

    private static int readVarInt(ByteBuf input, int maxBytes) {
        int out = 0;
        int bytes = 0;
        byte in;

        while (true) {
            in = input.readByte();

            out |= (in & 0x7F) << (bytes++ * 7);

            if (bytes > maxBytes) {
                throw new RuntimeException("VarInt too big");
            }

            if ((in & 0x80) != 0x80) {
                break;
            }
        }

        return out;
    }

    public static void writeVarInt(int value, ByteBuf output) {
        int part;

        while (true) {
            part = value & 0x7F;
            value >>>= 7;

            if (value != 0) {
                part |= 0x80;
            }

            output.writeByte(part);

            if (value == 0) {
                break;
            }
        }
    }

}
