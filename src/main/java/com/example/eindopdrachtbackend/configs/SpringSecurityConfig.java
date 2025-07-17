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
                                // User endpoints, copied from my tech-it-easy project.
                                //TODO: Replace with your own endpoints and roles

                                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                                // Television endpoints
                                .requestMatchers(HttpMethod.POST, "/televisions").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/televisions/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/televisions/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/televisions/**").hasAnyRole("ADMIN", "USER")

                                // Remote Controller endpoints
                                .requestMatchers(HttpMethod.POST, "/remotecontrollers").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/remotecontrollers/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/remotecontrollers/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/remotecontrollers/**").hasAnyRole("ADMIN", "USER")

                                // CI Module endpoints
                                .requestMatchers(HttpMethod.POST, "/cimodules").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/cimodules/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/cimodules/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/cimodules/**").hasAnyRole("ADMIN", "USER")

                                // Wall Bracket endpoints
                                .requestMatchers(HttpMethod.POST, "/wallbrackets").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/wallbrackets/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/wallbrackets/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/wallbrackets/**").hasAnyRole("ADMIN", "USER")

                                .requestMatchers("/authenticated").authenticated()
                                .requestMatchers("/authenticate").permitAll()
                                .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
