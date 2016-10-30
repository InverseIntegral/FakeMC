package ch.inverseintegral.fakemc.config;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationValues {

    private int currentPlayers;
    private int maxPlayers;
    private String motd;
    private String favicon;
    private String kickMessage;

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getMotd() {
        return motd;
    }

    public String getFavicon() {
        return favicon;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public void setKickMessage(String kickMessage) {
        this.kickMessage = kickMessage;
    }
}
