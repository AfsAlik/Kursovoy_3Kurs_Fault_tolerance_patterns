package demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${dependency.url}")
    private String dependencyUrl;

    @Value("${resttemplate.connect-timeout:2000}")
    private int connectTimeout;

    @Value("${resttemplate.read-timeout:2000}")
    private int readTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    public String getDependencyUrl() {
        return dependencyUrl;
    }
}