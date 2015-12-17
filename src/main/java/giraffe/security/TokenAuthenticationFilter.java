package giraffe.security;

import giraffe.service.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private TokenAuthenticationService tokenAuthenticationService;

    private final String headerName;


    @Autowired
    public TokenAuthenticationFilter(final String headerName,
                                     final TokenAuthenticationService tokenAuthenticationService) {
        this.headerName = headerName;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String token = ((HttpServletRequest) request).getHeader(headerName);
        if (token != null) {
            final GiraffeUserDetails userDetails = tokenAuthenticationService.parseUserFromToken(token);
            if (userDetails != null) {
                SecurityContextHolder.getContext().setAuthentication(new GiraffeAuthentication(userDetails));
            }
        }
        chain.doFilter(request, response);
    }

}
