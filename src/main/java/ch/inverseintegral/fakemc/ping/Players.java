package ch.inverseintegral.fakemc.ping;

import lombok.Data;

import java.util.List;

/**
 * Represents the player related data in a {@link StatusResponse status}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see StatusResponse
 */
@Data
public class Players {

    /**
     * The maximum amount of players.
     */
    private final int max;

    /**
     * The minimum amount of players.
     */
    private final int online;

    /**
     * The list of sample players.
     * This is currently showed when hovering over the player count.
     */
    private final List<Player> sample;

}