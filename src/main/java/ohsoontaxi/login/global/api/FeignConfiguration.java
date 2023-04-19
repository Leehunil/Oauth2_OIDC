package ohsoontaxi.login.global.api;

import feign.Logger;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static feign.Logger.*;

@EnableFeignClients(basePackages = "ohsoontaxi.login.global.api")
@Configuration
//@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FeignConfiguration {

    @Bean
    Level feginLoggerLevel() {
        return Level.FULL;
    }
}
