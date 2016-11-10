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

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

}
