package com.project.shopapp.configuration;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.models.User;
import com.project.shopapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;
//    user Detail object đối tượng theo chuẩn java spring đăng nhập theo no
    @Bean
    public UserDetailsService userDetailsService(){
        return phoneNumber->userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new UsernameNotFoundException("Ko tim thay phoneNumber: "+phoneNumber));

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManage(AuthenticationConfiguration  configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
