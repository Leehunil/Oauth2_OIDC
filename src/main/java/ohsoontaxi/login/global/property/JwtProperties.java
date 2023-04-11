package ohsoontaxi.login.global.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@AllArgsConstructor
@ConfigurationProperties("auth.jwt")
//@ConstructorBinding 스프링 3.0부터는 1개의 생성자만 있는 경우에 기본적으로 생성자 방식으로 동작하도록 수정-> 없어도 됨
public class JwtProperties {

    private final String secretKey;
    private final Long accessExp;
    private final Long refreshExp;
    private final String header;
    private final String prefix;
}