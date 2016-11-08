package ch.inverseintegral.fakemc.server.packets.handshake;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * The initial packet a client sends.
 * This packet contains some request data.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class Handshake extends Packet {

    private int protocolVersion;
    private String host;
    private int port;
    private int requestedProtocol;

    /**
     * Reflection constructor
     */
    public Handshake() {}

    @Override
    public void read(ByteBuf buf) {
        this.protocolVersion = readVarInt(buf);
        this.host = readString(buf);
        this.port = buf.readUnsignedShort();
        this.requestedProtocol = readVarInt(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        throw new UnsupportedOperationException("Cant write handshake");
    }

}
