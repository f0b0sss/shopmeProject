package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/users", "/users/**", "/settings", "/settings/**",
                        "/countries", "/countries/**", "/states", "/states/**").hasAuthority("Admin")
                .requestMatchers("/countries/{id}/states").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/categories", "/categories/**", "/brands", "/brands/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/products", "/products/**", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .requestMatchers("/products/save", "/products/edit/**", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
                .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                .requestMatchers("/customers/**", "/orders/**", "get_shipping_cost").hasAnyAuthority("Admin", "Salesperson")
                .requestMatchers("/orders", "/orders/page/**", "/orders/detail/**", "/orders_shipper/update/**").hasAnyAuthority("Admin", "Shipper", "Salesperson")
                .requestMatchers("/css/**", "/images/**", "/js/**", "/webfonts/**", "/fontawesome/**", "/webjars/**", "/richText/**").permitAll()
//                .anyRequest().permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
//                .defaultSuccessUrl("/")
//                .failureUrl("/login-error")
//                .loginProcessingUrl("/auth")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                .key("abcdefghijklmnopqrstuvwxyz_1234567890")
                .tokenValiditySeconds(7 * 24 * 60 * 60);
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/").deleteCookies("JSESSIONID")
//                .invalidateHttpSession(true)
//                .and()
//                .csrf().disable();
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }


}
