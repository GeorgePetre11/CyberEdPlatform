// DESIGN PATTERN: STRATEGY PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/config/SecurityConfig.java
//
// Description: Uses PasswordEncoder interface with BCrypt strategy. The encoding
// algorithm can be swapped at runtime or configuration time without changing client code.
//
// Pattern Elements:
// - Strategy Interface: PasswordEncoder (Spring Security interface)
// - Concrete Strategy: BCryptPasswordEncoder
// - Context: Spring Security authentication system
// - Alternative Strategies: Argon2PasswordEncoder, SCryptPasswordEncoder, NoOpPasswordEncoder, etc.

package org.example.cybersecurity_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
        ;
        return http.build();
    }

    /**
     * STRATEGY PATTERN: Returns a concrete implementation of PasswordEncoder interface
     * 
     * This method demonstrates the Strategy pattern by providing BCryptPasswordEncoder.
     * The encoding strategy can be easily changed by returning a different implementation:
     * - return new Argon2PasswordEncoder();
     * - return new SCryptPasswordEncoder();
     * - return new Pbkdf2PasswordEncoder();
     * 
     * All authentication code using PasswordEncoder remains unchanged regardless of
     * which concrete strategy is used.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // STRATEGY: BCryptPasswordEncoder is one concrete strategy
        return new BCryptPasswordEncoder();
        
        // Could easily switch to another strategy:
        // return new Argon2PasswordEncoder();
        // return new SCryptPasswordEncoder();
    }
}
