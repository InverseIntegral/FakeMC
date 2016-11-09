package ch.inverseintegral.fakemc.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@RestController
public class ConfigurationController {

    @RequestMapping("/configuration")
    public String configurationValues() {
        return "";
    }

}
