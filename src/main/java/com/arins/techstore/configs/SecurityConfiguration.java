package com.arins.techstore.configs;

import com.arins.techstore.cognito.CognitoLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Static resources
                        .requestMatchers("/", "/sign-up", "/delete-account", "/home", "/logout", "/products","/suppliers", "userproducts").permitAll() // Public pages
                        .anyRequest().authenticated() // Secure other pages
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/cognito") // Custom login page
                        .defaultSuccessUrl("/home", true) // Redirect to home after successful login
                        .failureUrl("/login?error=true") // Redirect to login with error on failure
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"));

        var logoutSuccessHandler = new CognitoLogoutSuccessHandler(clientRegistrationRepository);
        http.logout(c -> c.logoutSuccessHandler(logoutSuccessHandler));

        return http.build();
    }
}
