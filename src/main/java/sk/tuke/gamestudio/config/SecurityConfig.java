package sk.tuke.gamestudio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/register", "/logout",
                                "/css/**", "/js/**", "/images/**", "/play/**", "/comment/**"
                        ).permitAll()
                        .anyRequest().permitAll() // можно заменить на .authenticated() при необходимости
                )
                .csrf(csrf -> csrf.disable()) // отключаем CSRF для простоты
                .formLogin(form -> form.disable()) // отключаем стандартную форму входа Spring Security
                .httpBasic(httpBasic -> httpBasic.disable()); // и basic auth тоже

        return http.build();
    }
}
