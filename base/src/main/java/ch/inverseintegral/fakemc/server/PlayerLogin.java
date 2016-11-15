package ch.inverseintegral.fakemc.server;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class PlayerLogin {

    private final String username;

    public PlayerLogin(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerLogin that = (PlayerLogin) o;

        return username.equals(that.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
