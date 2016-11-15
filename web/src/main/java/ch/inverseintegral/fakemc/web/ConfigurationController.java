package ch.inverseintegral.fakemc.web;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationValues values;

    @RequestMapping
    public ConfigurationValues configurationValues() {
        return values;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ConfigurationValues replaceConfigurationValues(@Valid @RequestBody ConfigurationValues configurationValues) {

        values.setCurrentPlayers(configurationValues.getCurrentPlayers());
        values.setMaxPlayers(configurationValues.getMaxPlayers());
        values.setMotd(configurationValues.getMotd());
        values.setKickMessage(configurationValues.getKickMessage());

        return values;
    }

}
