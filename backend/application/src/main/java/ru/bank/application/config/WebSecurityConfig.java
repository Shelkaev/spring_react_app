package ru.bank.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


import ru.bank.application.security.JwtConfig;
import ru.bank.application.security.JwtTokenProvider;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT_TRANSFER = "/api/transfer/admin/**";
    private static final String ADMIN_ENDPOINT_CREDIT_CARD = "/api/cards/credit/admin/**";
    private static final String RATE_ENDPOINT = "/api/rate/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/login";
    private static final String REGISTRATION_ENDPOINT = "/api/registration";
    private static final String GET_RATE_ENDPOINT = "/api/rate/get/**";


        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors().and()
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(LOGIN_ENDPOINT).permitAll()
                    .antMatchers(REGISTRATION_ENDPOINT).permitAll()
                    .antMatchers(GET_RATE_ENDPOINT).hasAnyAuthority("USER", "ADMIN")
                    .antMatchers(ADMIN_ENDPOINT_TRANSFER).hasAuthority("ADMIN")
                    .antMatchers(ADMIN_ENDPOINT_CREDIT_CARD).hasAuthority("ADMIN")
                    .antMatchers(RATE_ENDPOINT).hasAuthority("ADMIN")
                    .anyRequest().hasAuthority("USER")
                    .and()
                    .apply(new JwtConfig(jwtTokenProvider));
        }
    }
