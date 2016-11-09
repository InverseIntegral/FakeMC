package ch.inverseintegral.fakemc.web;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@RestController
public class ConfigurationController {

    @Autowired
    private ConfigurationValues configurationValues;

    @RequestMapping("/configuration")
    public ConfigurationValues configurationValues() {
        return configurationValues;
    }

}
