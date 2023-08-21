package com.example.buysell.security;

import com.example.buysell.repository.UserRepository;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.security.authentication.GoogleSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    private final DataSource dataSource;
    private final UserRepository userRepository;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email, password, active from users where email=?"
        );


        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select email, role from roles where email=?"
        );

//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
//                "select r.email, r.role " +
//                        "from roles r " +
//                        "inner join users u on r.email = u.email " +
//                        "where u.email = ?"
//        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/static/**", "/", "/login/**", "/signup/**", "/processRegistration", "/oauth2/**", "/registrationConfirm").permitAll()
                                .requestMatchers("/product/create").hasAnyAuthority("USER")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login/google")
                                .successHandler(new GoogleSuccessHandler(userRepository))
                );

        return http.build();
    }

    @Bean
    public SecurityFilterChain emailFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login/email").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login/email")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                )
                .authenticationProvider(authenticationProvider());

        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EmailAuthenticationProvider authenticationProvider(){
        return new EmailAuthenticationProvider(userDetailsManager(dataSource), passwordEncoder());

    }
}




//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return daoAuthenticationProvider;

//    @Bean
//    public SecurityFilterChain facebookFilterChain(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests(configurer ->
//                        configurer
//                                .requestMatchers("/static/**").permitAll()
//                                .requestMatchers("/","/login**", "/signup**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(form ->
//                        form
//                                .loginPage("/login/facebook")
//                                .loginProcessingUrl("/authenticateTheUser")
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .logoutUrl("/logout")
//                                .logoutSuccessUrl("/")
//                )
////                .authenticationProvider(emailAuthenticationProvider());
//        ;
//
//        return http.build();
//    }