package ch.inverseintegral.fakemc.server.packets.login;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * This packet is sent when disconnecting a player from the server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class Kick extends Packet {

    /**
     * The kick message in this {@link ch.inverseintegral.fakemc.server.ping.Chat json format}.
     */
    private String message;

    /**
     * Reflection constructor
     */
    public Kick() {}

    public Kick(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Can't read kick packet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(ByteBuf buf) {
        writeString(this.message, buf);
    }

}
