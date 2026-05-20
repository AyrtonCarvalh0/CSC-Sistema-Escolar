package com.eiba.System_Finances.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(Customizer.withDefaults())
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
            )
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST,   "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST,   "/auth/cadastrar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,    "/auth/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/auth/usuarios/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/auth/usuarios/{id}/resetar-senha").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/auth/trocar-senha").authenticated()
                .requestMatchers(HttpMethod.GET,    "/audit/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/aluno/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/turmas/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/professores/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/responsavel/{id}").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
