package ch.inverseintegral.fakemc.server.ping;

/**
 * Represents a specific player.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see Players
 */
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

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
    }


}