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
                                // Authentication endpoints
                                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authenticated").authenticated()

                                // User registration and management
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/{username}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/{username}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/users/{username}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{username}/change-password").authenticated()

                                // Role/Authority management
                                .requestMatchers(HttpMethod.GET, "/users/{username}/authorities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/{username}/authorities").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/{username}/authorities/{authority}").hasRole("ADMIN")

                                // UserProfile endpoints
                                .requestMatchers(HttpMethod.GET, "/users/{username}/profile").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/users/{username}/profile").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/{username}/profile/avatar").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users/{username}/profile/avatar").permitAll()

                                // Game endpoints
                                .requestMatchers(HttpMethod.GET, "/games").permitAll()
                                .requestMatchers(HttpMethod.GET, "/games/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/games").hasAnyRole("ADMIN", "DEVELOPER")
                                .requestMatchers(HttpMethod.PUT, "/games/{id}").hasAnyRole("ADMIN", "DEVELOPER")
                                .requestMatchers(HttpMethod.DELETE, "/games/{id}").hasAnyRole("ADMIN", "DEVELOPER")

                                .requestMatchers(HttpMethod.GET, "/games/{gameId}/reviews").permitAll()
                                .requestMatchers(HttpMethod.POST, "/games/{gameId}/reviews").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/reviews/{reviewId}").authenticated()

                                .requestMatchers(HttpMethod.POST, "/reviews/{reviewId}/upvote").authenticated()
                                .requestMatchers(HttpMethod.POST, "/reviews/{reviewId}/downvote").authenticated()

                                // GameJam endpoints
                                .requestMatchers(HttpMethod.GET, "/gamejams").permitAll()
                                .requestMatchers(HttpMethod.GET, "/gamejams/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/gamejams").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/gamejams/{id}/join").authenticated()

                                // Email functionality
                                .requestMatchers(HttpMethod.POST, "/send").hasRole("ADMIN")

                                .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
