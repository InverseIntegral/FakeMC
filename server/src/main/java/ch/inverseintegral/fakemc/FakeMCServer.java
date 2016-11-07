package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import io.netty.bootstrap.ServerBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Component
class FakeMCServer {

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private ConfigurationValues configurationValues;

    /**
     * Starts the netty server on the given port.
     * @throws InterruptedException
     */
    void start() throws InterruptedException {
        serverBootstrap.bind(configurationValues.getPort())
                .sync()
                .channel()
                .closeFuture()
                .sync();
    }

}
