package com.example.reddit.clone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtProvider jwtProvider;

    @Qualifier( "userDetailsServiceImpl" )
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException
    {
        String jwtFromRequest = getJwtFromRequest( request );
        if( jwtFromRequest != null
                    && !jwtFromRequest.isBlank()
                    && jwtProvider.validateToken( jwtFromRequest )
                    && SecurityContextHolder.getContext().getAuthentication() == null )
        {
            String username = jwtProvider.extractUsername( jwtFromRequest );
            UserDetails userDetails = userDetailsService.loadUserByUsername( username );
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );

            authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

            SecurityContextHolder.getContext().setAuthentication( authenticationToken );
        }
        filterChain.doFilter( request, response );
    }

    private String getJwtFromRequest( HttpServletRequest request )
    {
        String bearerToken = request.getHeader( "Authorization" );
        if( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( "Bearer " ) )
            return bearerToken.substring( 7 );
        return bearerToken;
    }
}
