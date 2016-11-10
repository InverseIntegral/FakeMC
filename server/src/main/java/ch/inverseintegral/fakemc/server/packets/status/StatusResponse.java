package ch.inverseintegral.fakemc.server.packets.status;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;

/**
 * Response packet to the {@link StatusRequest status request}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class StatusResponse extends Packet {

    public String response;

    public StatusResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

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
