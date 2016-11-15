package ch.inverseintegral.fakemc.server.ping;

/**
 * This object represents a chat message.
 * Normally multiple formatting possibilities would be available here.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see ch.inverseintegral.fakemc.server.packets.login.Kick
 * @see StatusResponse
 */
public class Chat {

    /**
     * The text message.
     * This can include minecraft formatting codes.
     */
    private final String text;

    public Chat(String text) {
        this.text = text;
    }

}