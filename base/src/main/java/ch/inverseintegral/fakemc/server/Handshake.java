package ch.inverseintegral.fakemc.server;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class Handshake {

    private final int protocolVersion;
    private final String host;
    private final int port;

    public Handshake(int protocolVersion, String host, int port) {
        this.protocolVersion = protocolVersion;
        this.host = host;
        this.port = port;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Handshake handshake = (Handshake) o;

        return protocolVersion == handshake.protocolVersion &&
                port == handshake.port &&
                host.equals(handshake.host);

    }

    @Override
    public int hashCode() {
        int result = protocolVersion;
        result = 31 * result + host.hashCode();
        result = 31 * result + port;
        return result;
    }
}
