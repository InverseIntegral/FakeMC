package ch.inverseintegral.fakemc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holds the configured server values.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties
public class ConfigurationValues {

    private int currentPlayers;
    private int maxPlayers;
    private String motd;
    private String kickMessage;
    private int port;

}
