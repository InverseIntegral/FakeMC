package ch.inverseintegral.fakemc.server;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
public class ServerStatistics {

    private Set<Handshake> handshakes;

    public ServerStatistics() {
        this.handshakes = new HashSet<>();
    }

    public void addHandshake(String host, int port, int protocolVersion) {
        Set<Handshake> equalHandshakes = handshakes.stream()
                .filter(handshake -> handshake.getHost().equals(host) && handshake.getPort() == port)
                .collect(Collectors.toSet());

        if (equalHandshakes.isEmpty()) {
            handshakes.add(new Handshake(protocolVersion, host, port));
        } else {
            equalHandshakes.forEach(Handshake::increaseAmount);
        }
    }

    public Set<Handshake> getHandshakes() {
        return handshakes;
    }

    static class Handshake {

        private final int protocolVersion;
        private final String host;
        private final int port;
        private int amount;

        public Handshake(int protocolVersion, String host, int port) {
            this.protocolVersion = protocolVersion;
            this.host = host;
            this.port = port;
            this.amount = 1;
        }

        public int getProtocolVersion() {
            return protocolVersion;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public int getAmount() {
            return amount;
        }

        void increaseAmount() {
            amount++;
        }

    }

}
