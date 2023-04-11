//package ohsoontaxi.login.global.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import ohsoontaxi.login.global.security.JwtTokenProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.formLogin().disable().cors().and().csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers()
//                .permitAll()
//                .antMatchers("/api/v1/credentials/me", "/api/v1/credentials/logout")
//                .permitAll()
//                .antMatchers("/api/v1/credentials/**")
//                .permitAll()
//                .antMatchers("/api/v1/asset/version")
//                .permitAll()
//                .antMatchers("/api/v1/notifications/experience")
//                .permitAll()
//                .anyRequest()
//                .authenticated();
//
//        http.apply(new FilterConfig(jwtTokenProvider, objectMapper));
//
//        return http.build();
//    }
//
//}
