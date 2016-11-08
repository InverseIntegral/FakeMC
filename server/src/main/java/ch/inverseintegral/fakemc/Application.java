package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.server.FakeMCServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * This class is the entry point of the application.
 * It does configure some netty objects and invokes the server
 * {@link FakeMCServer#start() start}.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        context.getBean(FakeMCServer.class).start();
    }

}
