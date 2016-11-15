package ch.inverseintegral.fakemc.server.packets.status;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * An empty status requests packet.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StatusRequest extends Packet {

    public StatusRequest() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(ByteBuf buf) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(ByteBuf buf) {}

}
