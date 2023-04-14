package ohsoontaxi.login.global.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static feign.Logger.*;

@EnableFeignClients(basePackages = "ohsoontaxi.login.global.config")
@Configuration
public class FeignConfiguration {

    @Bean
    Level feginLoggerLevel() {
        return Level.FULL;
    }
}
