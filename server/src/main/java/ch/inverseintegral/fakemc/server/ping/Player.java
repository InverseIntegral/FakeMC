package ch.inverseintegral.fakemc.server.ping;

import lombok.Data;

/**
 * Represents a specific player.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see Players
 */
@Data
public class Player {

    /**
     * The name of this player.
     */
    private final String name;

    /**
     * The uuid string representation of this player.
     * Can be a random uuid.
     */
    private final String id;

}