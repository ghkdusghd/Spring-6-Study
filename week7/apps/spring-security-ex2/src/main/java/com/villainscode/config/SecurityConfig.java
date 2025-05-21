package com.villainscode.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/shop/**")
                        .hasRole("MANAGER")
                        .requestMatchers("/shop/**").hasAnyRole("MANAGER", "EMPLOYEE")
                        .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/shop/list")
                        .failureUrl("/login?failure")
                        .permitAll()
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/display-access-denied"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user1234")
                .roles("MANAGER")
                .build();

        UserDetails user1 = User.builder()
                .username("user1")
                .password("{noop}user1234")
                .roles("EMPLOYEE")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password("{noop}user1234")
                .roles("GUEST")
                .build();

        return new InMemoryUserDetailsManager(user, user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
