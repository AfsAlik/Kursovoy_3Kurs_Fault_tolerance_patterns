package demo;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import demo.config.AppConfig;

@Service
public class DependencyClient {

    private static final Logger logger = LoggerFactory.getLogger(DependencyClient.class);
    private final RestTemplate restTemplate;
    private final String dependencyUrl;

    @Autowired
    public DependencyClient(RestTemplate restTemplate, AppConfig appConfig) {
        this.restTemplate = restTemplate;
        this.dependencyUrl = appConfig.getDependencyUrl();
    }

    // Важен порядок: Retry должен быть ПЕРВЫМ, затем CircuitBreaker с fallback
    @Retry(name = "myRetry")
    @CircuitBreaker(name = "myCircuitBreaker")  // fallbackMethod убран
    @Bulkhead(name = "myBulkhead")
    public String callDependency(String failMode) {
        String url = dependencyUrl;
        if (failMode != null && !failMode.isBlank()) {
            url += "?fail=" + failMode;
        }
        logger.info("Calling dependency at: {}", url);
        return restTemplate.getForObject(url, String.class);
    }

    @SuppressWarnings("unused")
    private String fallback(String failMode, Throwable t) {
        logger.error("Fallback triggered for failMode={}, error: {}", failMode, t.getMessage());
        return "FALLBACK: Service unavailable";
    }
}