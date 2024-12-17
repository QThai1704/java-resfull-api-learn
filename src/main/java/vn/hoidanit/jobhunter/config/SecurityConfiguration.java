package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Phân quyền
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,
                        CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
                String[] whiteList = {
                                "/", "/api/v1/auth/**", "/storage/**", "api/v1/emails",
                                "/api/v1/companies/**", "/api/v1/auth/jobs/**", "/api/v1/auth/skills/**", "/api/v1/files/**",
                                "/api/v1/subcribers/**", "/api/v1/auth/resumes/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                };
                http
                                .csrf(c -> c.disable())
                                .cors(Customizer.withDefaults())
                                .authorizeHttpRequests(
                                                authz -> authz
                                                                .requestMatchers(whiteList).permitAll()
                                                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/companies/**").permitAll()
                                                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/jobs/**").permitAll()
                                                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/skills/**").permitAll()
                                                                .anyRequest().authenticated())
                                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                                .formLogin(f -> f.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }

}
