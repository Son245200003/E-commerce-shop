package com.project.shopapp.configuration;

import com.project.shopapp.filter.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;
    private final JwtTokenFilter jwtTokenFilter;
//    LỌc trước khi vào đuường dẫn (ông bảo vệ)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
         httpSecurity.csrf(AbstractHttpConfigurer::disable)
                 .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                 .authorizeHttpRequests(request -> {
                     request
                             .requestMatchers(
                                     String.format("%s/users/register", apiPrefix),
                                     String.format("%s/users/login", apiPrefix),
                                     String.format("%s/healthcheck/**", apiPrefix),
                                     String.format("%s/actuator/**", apiPrefix)

                             )
                             .permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/roles", apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/categories", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/categories/**", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//PRODUCT

                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products**", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/imageProduct/**", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/image/**", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/products/**", apiPrefix)).permitAll()

                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/products/image**", apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/products**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//OrderDetails
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER,Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//ORder
//                             .requestMatchers(HttpMethod.GET,
//                                     String.format("%s/orders/get-orders-by-keyword", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.POST,
                                     String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
                             .requestMatchers(HttpMethod.GET,
                                     String.format("%s/orders/**", apiPrefix)).permitAll()
                             .requestMatchers(HttpMethod.DELETE,
                                     String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                             .requestMatchers(HttpMethod.PUT,
                                     String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                             .anyRequest().permitAll();
                 })
                 .csrf(AbstractHttpConfigurer::disable);
//        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
//                CorsConfiguration configure=new CorsConfiguration();
//                configure.setAllowedOrigins(List.of("*"));
//                configure.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
//                configure.setAllowedHeaders(Arrays.asList("Content-Type","authorization","x-auth-token"));
//                configure.setExposedHeaders(List.of("x-auth-token"));
//                UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//                source.registerCorsConfiguration("/**",configure);
//                httpSecurityCorsConfigurer.configurationSource(source);
//            }
//        })
         ;

         return httpSecurity.build();

    }
}
