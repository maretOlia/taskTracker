package giraffe;

import giraffe.security.TokenizedUsernamePasswordAuthenticationFilter;
import giraffe.security.TokenAuthenticationFilter;
import giraffe.service.security.PrivateUserDetailsService;
import giraffe.service.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()  .anonymous()

        .and()  .authorizeRequests()

                // allow anonymous access
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/private/account").permitAll()
                .antMatchers(HttpMethod.GET, "/private/account").permitAll()
                .antMatchers(HttpMethod.POST, "/private/login").permitAll()

                //defined Admin only API area
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
        .and()
                // login-pass based authentication with post-processing custom filter which adds auth token
                .addFilterBefore(new TokenizedUsernamePasswordAuthenticationFilter("/private/login", AUTH_HEADER_NAME, tokenAuthenticationService, privateUserDetailsService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                // custom stateless token based authentication
                .addFilterBefore(new TokenAuthenticationFilter(AUTH_HEADER_NAME, tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(privateUserDetailsService);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return privateUserDetailsService;
    }
}
