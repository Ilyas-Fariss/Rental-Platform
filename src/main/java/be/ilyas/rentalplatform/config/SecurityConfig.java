package be.ilyas.rentalplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // BCrypt voor veilige wachtwoorden
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF uit voor eenvoud (in een echte app zou je dit beter configureren)
                .csrf(AbstractHttpConfigurer::disable)

                // Toegangsregels
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/catalog",
                                "/register",
                                "/login",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        // winkelmandje en checkout enkel voor ingelogde users
                        .requestMatchers("/cart/**").authenticated()
                        .anyRequest().authenticated()
                )

                // Form-based login met eigen loginpagina
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/catalog", true)
                        .permitAll()
                )

                // Logout-configuratie
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/catalog")
                        .permitAll()
                );

        // H2-console toestaan in een frame
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        return http.build();
    }
}
