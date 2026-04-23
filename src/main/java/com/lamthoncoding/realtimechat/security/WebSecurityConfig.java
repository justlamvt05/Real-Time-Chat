package com.lamthoncoding.realtimechat.security;



import com.lamthoncoding.realtimechat.security.config.auth.AuthEntryPointJwt;
import com.lamthoncoding.realtimechat.security.config.auth.CustomAccessDenied;
import com.lamthoncoding.realtimechat.security.config.jwt.AuthChannelInterceptor;
import com.lamthoncoding.realtimechat.security.config.jwt.AuthTokenFilter;
import com.lamthoncoding.realtimechat.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;
    private final CustomAccessDenied accessDenied;
    private final AuthChannelInterceptor authChannelInterceptor;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable()
        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/oauth2/**",
                                "/auth/**",
                                "/ws/**"
                        ).permitAll()
                        .requestMatchers("/chat/**, /messages/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
//                .oauth2Login(oauth -> oauth
//                        .loginPage("/oauth2/authorization/google")
//                )

//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests.requestMatchers("/auth/**","/hello", "/error", "/all").permitAll()
//                                .requestMatchers("/user/profile").hasAnyRole("ADMIN", "SALE", "CUSTOMER")
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/sale/**").hasRole("SALE")
//                                .requestMatchers("/user/**").hasRole("CUSTOMER")
//                                .requestMatchers("/cart/**").permitAll()
//                                .requestMatchers("/products/**").permitAll()
//                                .requestMatchers("/uploads/**").permitAll()
//                                .anyRequest().authenticated())
//
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint(unauthorizedHandler)
//                        .accessDeniedHandler(accessDenied)
//
//                )

                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class).build();

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
        registration.taskExecutor()
                .corePoolSize(10)
                .maxPoolSize(50)
                .queueCapacity(1000);

    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }
}
