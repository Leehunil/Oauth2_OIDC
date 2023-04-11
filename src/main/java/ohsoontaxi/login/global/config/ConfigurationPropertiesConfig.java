package ohsoontaxi.login.global.config;

import ohsoontaxi.login.global.property.JwtProperties;
import ohsoontaxi.login.global.property.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtProperties.class, OauthProperties.class})
@Configuration
public class ConfigurationPropertiesConfig {
}
