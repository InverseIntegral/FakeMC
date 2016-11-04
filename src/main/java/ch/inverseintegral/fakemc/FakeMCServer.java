package ch.inverseintegral.fakemc;

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

    void start() throws InterruptedException {
        serverBootstrap.bind(25565)
                .sync()
                .channel()
                .closeFuture()
                .sync();
    }

}
