package be.ilyas.rentalplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF voorlopig uit zodat we geen problemen hebben met simpele formuliertjes
                .csrf(csrf -> csrf.disable())
                // Alle requests voorlopig toestaan
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // Voor nu geen loginpagina, geen http basic nodig
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login.disable());

        return http.build();
    }
}
