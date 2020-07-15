package com.example.reddit.clone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtProvider
{
    private static final String SECRET_KEY = "1CB8339992E25017C8DF079941910047952FDA16A8012BEFB5D05E26BCFBD830";

    public String generateToken( Authentication authentication )
    {
        User principal = ( User ) authentication.getPrincipal();

        return Jwts.builder()
                   .setSubject( principal.getUsername() )
                   .signWith( SignatureAlgorithm.HS256, SECRET_KEY )
                   .compact();
    }

    boolean validateToken( String jwtFromRequest )
    {
        Jwts.parser().setSigningKey( SECRET_KEY ).parseClaimsJws( jwtFromRequest );
        return true;
    }

    private String createToken( Map<String,Object> claims, String subject )
    {
        return Jwts.builder().setClaims( claims ).setSubject( subject )
                   .setIssuedAt( new Date( System.currentTimeMillis() ) )
                   .setExpiration( new Date( System.currentTimeMillis() + 1000 * 60 * 60 ) )
                   .signWith( SignatureAlgorithm.HS256, SECRET_KEY )
                   .compact();
    }

    private boolean isTokenExpired( String token )
    {
        return extractExpiration( token ).before( new Date() );
    }

    private Claims extractAllClaims( String token )
    {
        return Jwts.parser().setSigningKey( SECRET_KEY ).parseClaimsJws( token ).getBody();
    }

    private <R> R extractClaim( String token, Function<Claims,R> claimsResolver )
    {
        final Claims claims = extractAllClaims( token );
        return claimsResolver.apply( claims );
    }

    private Date extractExpiration( String token )
    {
        return extractClaim( token, Claims::getExpiration );
    }

    String extractUsername( String token )
    {
        return extractClaim( token, Claims::getSubject );
    }

}
