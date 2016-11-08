package ch.inverseintegral.fakemc.server.packets.status;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * Response packet to the {@link StatusRequest status request}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class StatusResponse extends Packet {

    private final String response;

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(ByteBuf buf) {
        throw new UnsupportedOperationException("Can't read status response packet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(ByteBuf buf) {
        writeString(this.response, buf);
    }

}
