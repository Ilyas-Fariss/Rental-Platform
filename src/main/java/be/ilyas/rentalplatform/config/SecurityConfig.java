package be.ilyas.rentalplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                // ✅ CSRF AAN laten (veilig), maar H2-console uitzonderen
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // Toegangsregels
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/dashboard",          // dashboard publiek bereikbaar (zoals jij wil)
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

                        // profiel + orders enkel voor ingelogde users
                        .requestMatchers("/profile/**", "/orders/**").authenticated()

                        .anyRequest().authenticated()
                )

                // Form-based login met eigen loginpagina
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                // Logout-configuratie
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/dashboard")
                        .permitAll()
                );

        // H2-console toestaan in een frame
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        return http.build();
    }
}
