package br.com.senacsp.tads.stads4ma.library.infrascrture.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserDatailsService userDatailsService;

    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher) throws Exception{
        return
                http
                        .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/console/**")
                                .disable())
                        .headers(headers -> headers
                                .frameOptions(frame -> frame.disable()))
                                        .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                   "/auth/**",
                                                   "/auth/",
                                                   "/v3/api-docs",
                                                   "/swagger-ui",
                                                   "/console/**"
                                                ).permitAll()
                                                .anyRequest()
                                                .authenticated())
                        .sessionManagement(sessionManagment -> sessionManagment)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
    @Bean
    private AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsPasswordService(this.userDatailsService);
        provider.setPasswordEncoder(passwordEnconder());
        return provider;
    }
    @Bean
    private  PasswordEnconder passwordEnconder(){return new BCryptPasswordEncoder();}
    @Bean
    private AuthenticationManager authenticationManager(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(this.userDatailsService);
        authProvider.setPasswordEncoder(passwordEnconder());
        return new ProviderManager(authProvider);
    }

    }

    }
