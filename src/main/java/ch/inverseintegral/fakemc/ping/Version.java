package ch.inverseintegral.fakemc.ping;

import lombok.Data;

/**
 * Represents the protocol and server version.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see StatusResponse
 */
@Data
public class Version {

    private final String name;
    private final int protocol;

}
