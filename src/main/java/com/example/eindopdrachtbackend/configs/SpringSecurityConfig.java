package com.example.eindopdrachtbackend.configs;

import com.example.eindopdrachtbackend.filters.JwtRequestFilter;
import com.example.eindopdrachtbackend.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }



    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter (HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->
                        auth
                                // User endpoints
                                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                                // Game endpoints
                                .requestMatchers(HttpMethod.POST, "/games").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/games/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/games/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/games/**").hasAnyRole("ADMIN", "USER")

                                // Game Review endpoints
                                .requestMatchers(HttpMethod.POST, "/reviews").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/reviews/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasAnyRole("ADMIN", "USER")

                                // Review Vote endpoints
                                .requestMatchers(HttpMethod.POST, "/reviews/*/vote").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/reviews/*/vote").hasAnyRole("ADMIN", "USER")

                                // Game Jam endpoints
                                .requestMatchers(HttpMethod.POST, "/gamejams").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/gamejams/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/gamejams/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/gamejams/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/gamejams/*/participate").hasAnyRole("ADMIN", "USER")

                                // Email endpoint
                                .requestMatchers(HttpMethod.POST, "/send").hasAnyRole("ADMIN", "USER")

                                .requestMatchers("/authenticated").authenticated()
                                .requestMatchers("/authenticate").permitAll()
                                .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
