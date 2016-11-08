package ch.inverseintegral.fakemc.server.packets.login;

import ch.inverseintegral.fakemc.server.packets.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * This request is sent when joining a server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class LoginRequest extends Packet {

    /**
     * Contains some data. Not sure what..
     */
    private String data;

    /**
     * Reflection constructor
     */
    public LoginRequest() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(ByteBuf buf) {
        this.data = readString(buf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(ByteBuf buf) {
        writeString(this.data, buf);
    }

}
