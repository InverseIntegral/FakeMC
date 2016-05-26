package ch.inverseintegral.fakemc.packets.login;

import ch.inverseintegral.fakemc.packets.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * This packet is sent when disconnecting a player from the server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class Kick extends Packet {

    /**
     * The kick message in this {@link ch.inverseintegral.fakemc.ping.Chat json format}.
     */
    private final String message;

    /**
     * Reflection constructor
     */
    public Kick() {
        this.message = null;
    }

    public Kick(String message) {
        this.message = message;
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
