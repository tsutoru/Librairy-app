package librairy.app.containe.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setConnectTimeout(java.time.Duration.ofSeconds(5))
        .setReadTimeout(java.time.Duration.ofSeconds(5))
        .build();
  }
}
