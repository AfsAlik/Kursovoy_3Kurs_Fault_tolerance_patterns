package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private DependencyClient dependencyClient;

    @GetMapping("/call")
    public String call(@RequestParam(required = false) String fail) {
        try {
            return dependencyClient.callDependency(fail);
        } catch (Exception e) {
            logger.error("Request failed: {}", e.getMessage());
            return "FALLBACK: Service unavailable";
        }
    }
}