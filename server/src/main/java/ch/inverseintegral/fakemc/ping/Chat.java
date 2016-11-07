package ch.inverseintegral.fakemc.ping;

import lombok.Data;

/**
 * This object represents a chat message.
 * Normally multiple formatting possibilities would be available here.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 * @see ch.inverseintegral.fakemc.packets.login.Kick
 * @see StatusResponse
 */
@Data
public class Chat {

    /**
     * The text message.
     * This can include minecraft formatting codes.
     */
    private final String text;

}