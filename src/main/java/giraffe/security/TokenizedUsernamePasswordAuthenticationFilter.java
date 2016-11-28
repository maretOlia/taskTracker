package giraffe.security;

import giraffe.service.security.PrivateUserDetailsService;
import giraffe.service.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class TokenizedUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;

    private PrivateUserDetailsService privateUserDetailsService;

    private final String headerName;


    private static final long FIVE_DAYS = 1000 * 60 * 60 * 24 * 5;

    private static final String USERNAME_PARAM = "username";

    private static final String PASSWORD_PARAM = "password";


    @Autowired
    public TokenizedUsernamePasswordAuthenticationFilter(final String url,
                                                         final String headerName,
                                                         final TokenAuthenticationService tokenAuthenticationService,
                                                         final PrivateUserDetailsService userDetailsService,
                                                         final AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        this.headerName = headerName;
        this.privateUserDetailsService = userDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        setAuthenticationManager(authManager);
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = Optional.ofNullable(request.getParameter(USERNAME_PARAM)).orElse("");
        String password = Optional.ofNullable(request.getParameter(PASSWORD_PARAM)).orElse("");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username.trim(), password.trim());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authentication) throws IOException, ServletException {
        final GiraffePrivateUserDetails authenticatedUser = privateUserDetailsService.loadUserByUsername(authentication.getName());
        final GiraffeAuthentication userAuthentication = new GiraffeAuthentication(authenticatedUser);

        authenticatedUser.setExpires(System.currentTimeMillis() + FIVE_DAYS);
        response.addHeader(headerName, tokenAuthenticationService.createTokenForUser(authenticatedUser));

        // add the authentication to the Security context
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }

}
