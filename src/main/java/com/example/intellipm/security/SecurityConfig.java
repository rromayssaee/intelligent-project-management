package com.example.intellipm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Ressources statiques
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // Authentification
                        .requestMatchers("/api/auth/**")
                        .permitAll()


                        // Pages HTML
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/dashboard",
                                "/projects",
                                "/tasks",
                                "/teams",
                                "/users",
                                "/reports"
                        ).permitAll()

                        // API USERS — GET autorisé pour CHEF_PROJET (pour gérer les membres d'équipe)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/users", "/api/users/**")
                        .hasAnyRole("ADMIN", "CHEF_PROJET")
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // API TEAMS — GET autorisé pour MEMBRE (voir son équipe)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/teams", "/api/teams/**")
                        .hasAnyRole("ADMIN", "CHEF_PROJET", "MEMBRE")
                        .requestMatchers("/api/teams/**")
                        .hasAnyRole("ADMIN", "CHEF_PROJET")

                        // API PROJECTS
                        .requestMatchers("/api/projects/**")
                        .hasAnyRole(
                                "ADMIN",
                                "CHEF_PROJET",
                                "MEMBRE"
                        )

                        // API TASKS
                        .requestMatchers("/api/tasks/**")
                        .hasAnyRole(
                                "ADMIN",
                                "CHEF_PROJET",
                                "MEMBRE"
                        )

                        // API REPORTS
                        .requestMatchers("/api/reports/**")
                        .hasAnyRole(
                                "ADMIN",
                                "CHEF_PROJET"
                        )

                        // API DASHBOARD
                        .requestMatchers("/api/dashboard/**")
                        .hasAnyRole(
                                "ADMIN",
                                "CHEF_PROJET",
                                "MEMBRE"
                        )

                        .anyRequest()
                        .authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        customUserDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}