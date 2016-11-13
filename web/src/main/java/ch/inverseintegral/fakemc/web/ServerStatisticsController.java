package ch.inverseintegral.fakemc.web;

import ch.inverseintegral.fakemc.server.ServerStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/statistics")
public class ServerStatisticsController {

    @Autowired
    private ServerStatistics serverStatistics;

    @RequestMapping
    public ServerStatistics serverStatistics() {
        return serverStatistics;
    }

}
