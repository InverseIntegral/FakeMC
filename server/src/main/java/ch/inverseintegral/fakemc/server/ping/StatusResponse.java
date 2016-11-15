package ch.inverseintegral.fakemc.server.ping;

/**
 * This class contains a status response that is sent
 * to a client using the {@link ch.inverseintegral.fakemc.server.packets.status.StatusResponse response packet}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */

public class StatusResponse {

    /**
     * The server list motd.
     */
    private final Chat description;

    /**
     * The player object. (min and max players)
     */
    private final Players players;

    /**
     * The current version.
     */
    private final Version version;

    /**
     * The base64 icon string.
     */
    private final String favicon;

    public StatusResponse(Chat description, Players players, Version version, String favicon) {
        this.description = description;
        this.players = players;
        this.version = version;
        this.favicon = favicon;
    }
}