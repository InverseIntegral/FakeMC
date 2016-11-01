package ch.inverseintegral.fakemc.packets.status;

import ch.inverseintegral.fakemc.packets.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * A ping packet that contains the request time or some random data.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class Ping extends Packet {

    /**
     * The timestamp when the packet has been sent.
     * This will be used to calculate the ping.
     */
    private long time;

    /**
     * Reflection constructor
     */
    public Ping() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(ByteBuf buf) {
        this.time = buf.readLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(time);
    }

}
