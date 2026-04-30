package com.apiintegration.hngstage1profileaggregator.config.SecurityConfig;

import com.apiintegration.hngstage1profileaggregator.config.ApiVersionFilter;
import com.apiintegration.hngstage1profileaggregator.config.JwtConfig.JwtConfiguration;
import com.apiintegration.hngstage1profileaggregator.config.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfigurations {
    @Autowired
    private JwtConfiguration jwtConfiguration;
    @Autowired
    private RateLimitFilter rateLimitFilter;
    @Autowired
    private ApiVersionFilter apiVersionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors->corsConfigurationSource())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session-> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/","/api").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(apiVersionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtConfiguration, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            throw new UnsupportedOperationException("No authentication manager needed");
        };
    }

}
