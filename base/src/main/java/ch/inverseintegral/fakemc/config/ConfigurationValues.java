package ch.inverseintegral.fakemc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holds the configured server values.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
@ConfigurationProperties
public class ConfigurationValues {

    private int currentPlayers;
    private int maxPlayers;
    private String motd;
    private String kickMessage;
    private int port;
    private String favicon;

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getMotd() {
        return motd;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public int getPort() {
        return port;
    }

    public String getFavicon() {
        return favicon;
    }
}
