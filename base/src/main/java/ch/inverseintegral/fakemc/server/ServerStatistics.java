package ch.inverseintegral.fakemc.server;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
public class ServerStatistics {

    private Map<Handshake, Integer> handshakes;
    private Map<PlayerLogin, Integer> playerLogins;

    public ServerStatistics() {
        this.handshakes = new HashMap<>();
        this.playerLogins = new HashMap<>();
    }

    public void addHandshake(String host, int port, int protocolVersion) {
        Handshake handshake = new Handshake(protocolVersion, host, port);
        defaultIncrease(handshakes, handshake);
    }

    public Map<Handshake, Integer> getHandshakes() {
        return handshakes;
    }

    public void addPlayerLogin(String username) {
        PlayerLogin login = new PlayerLogin(username);
        defaultIncrease(playerLogins, login);
    }

    public Map<PlayerLogin, Integer> getPlayerLogins() {
        return playerLogins;
    }

    private <T> void defaultIncrease(Map<T, Integer> map, T key) {
        Integer current = map.getOrDefault(key, 0);
        map.put(key, ++current);
    }

}
