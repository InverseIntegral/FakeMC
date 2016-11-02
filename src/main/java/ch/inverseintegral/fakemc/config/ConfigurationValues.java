package ch.inverseintegral.fakemc.config;

import lombok.Data;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Data
public class ConfigurationValues {

    private int currentPlayers;
    private int maxPlayers;
    private String motd;
    private String kickMessage;
    private int port;

    public ConfigurationValues() {}

}
