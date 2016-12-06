package giraffe.config.security;

import giraffe.security.TokenAuthenticationFilter;
import giraffe.security.TokenizedUsernamePasswordAuthenticationFilter;
import giraffe.service.security.PrivateUserDetailsService;
import giraffe.service.security.TokenAuthenticationService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
@ComponentScan(basePackages = {"giraffe"})
@EnableWebSecurity
public class GiraffeSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    PrivateUserDetailsService privateUserDetailsService;

   /* @Autowired
    PasswordEncoder passwordEncoder;*/

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*"); //default db url: "jdbc:h2:mem:testdb"
        return registrationBean;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().anonymous()

                .and().authorizeRequests()

                // allow anonymous access
                .antMatchers("/console/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers(HttpMethod.POST, "/account").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()

                //defined Admin only API area
                .antMatchers("/admin/**").hasRole("ADMIN")

                //defined USER only API area
                .anyRequest().hasRole("USER")
                .and()

                // auth filter chain
                // login-pass based authentication with post-processing custom filter which adds auth token
                .addFilterBefore(new TokenizedUsernamePasswordAuthenticationFilter("login", AUTH_HEADER_NAME, tokenAuthenticationService, privateUserDetailsService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                // custom stateless token based authentication
                .addFilterBefore(new TokenAuthenticationFilter(AUTH_HEADER_NAME, tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);

        //TODO: add single Sign On auth

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(privateUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return privateUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
