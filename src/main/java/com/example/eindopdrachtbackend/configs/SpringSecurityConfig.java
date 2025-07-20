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
                                // Public endpoints
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                                .requestMatchers("/authenticated").authenticated()

                                // Game endpoints
                                .requestMatchers(HttpMethod.GET, "/games/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/games").hasAnyRole("ADMIN", "DEVELOPER")
                                .requestMatchers(HttpMethod.PUT, "/games/**").hasAnyRole("ADMIN", "DEVELOPER")
                                .requestMatchers(HttpMethod.DELETE, "/games/**").hasAnyRole("ADMIN", "DEVELOPER")

                                .requestMatchers(HttpMethod.GET, "/games/*/reviews").permitAll()
                                .requestMatchers(HttpMethod.GET, "/games/*/reviews/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/games/*/reviews").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/games/*/reviews/*").authenticated()
                                .requestMatchers(HttpMethod.POST, "/games/*/reviews/*/upvote").authenticated()
                                .requestMatchers(HttpMethod.POST, "/games/*/reviews/*/downvote").authenticated()

                                // UserProfile endpoints
                                .requestMatchers(HttpMethod.GET, "/users/*/profile").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/*/profile").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/*/profile/avatar").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/*/profile/avatar").permitAll()

                                // User management endpoints
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                                // Individual user access
                                .requestMatchers(HttpMethod.GET, "/users/{username}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/{username}").authenticated()

                                // Role management
                                .requestMatchers("/users/*/authorities/**").hasRole("ADMIN")

                                // Game Jam endpoints (future implementation)
                                .requestMatchers(HttpMethod.GET, "/gamejams/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/gamejams").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/gamejams/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/gamejams/**").hasRole("ADMIN")

                                // Email functionality
                                .requestMatchers("/send").hasRole("ADMIN")

                                .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
