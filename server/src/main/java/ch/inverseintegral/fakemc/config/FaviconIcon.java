package ch.inverseintegral.fakemc.config;

import ch.inverseintegral.fakemc.FakeMC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.URL;
import java.util.Base64;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class FaviconIcon {

    @Autowired
    private ConfigurationValues configuration;

    @Bean
    public String favicon() throws IOException {
        File iconFile = new File(getResourceFile(configuration.getFavicon()));

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(iconFile))) {
            int length = (int) iconFile.length();
            byte[] bytes = new byte[length];

            reader.read(bytes, 0, length);
            reader.close();

            StringBuilder builder = new StringBuilder("data:image/png;base64,");
            builder.append(Base64.getEncoder().encodeToString(bytes));

            return builder.toString();
        }
    }

    private String getResourceFile(String resource) throws FileNotFoundException {
        ClassLoader classLoader = FakeMC.class.getClassLoader();
        URL resourceURL = classLoader.getResource(resource);

        if (resourceURL == null) {
            throw new FileNotFoundException(resource + " file not found");
        }

        return resourceURL.getFile();
    }

}
