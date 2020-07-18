package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.AuthenticationResponse;
import com.example.reddit.clone.dto.LoginRequest;
import com.example.reddit.clone.dto.RefreshTokenRequest;
import com.example.reddit.clone.dto.RegisterRequest;
import com.example.reddit.clone.exception.SpringRedditCloneException;
import com.example.reddit.clone.model.NotificationMail;
import com.example.reddit.clone.model.User;
import com.example.reddit.clone.model.VerificationToken;
import com.example.reddit.clone.repository.UserRepository;
import com.example.reddit.clone.repository.VerificationTokenRepository;
import com.example.reddit.clone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AuthService
{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp( RegisterRequest registerRequest )
    {
        try
        {
            User user = new User();
            user.setUsername( registerRequest.getUsername() );
            user.setEmail( registerRequest.getEmail() );
            user.setPassword( passwordEncoder.encode( registerRequest.getPassword() ) );
            user.setCreatedDate( Instant.now() );
            user.setEnabled( false );

            userRepository.save( user );

            String token = generateVerificationToken( user );
            mailService.sendMail( new NotificationMail( "Please activate your account", user.getEmail(),
                    "Thank you for sign up with the Spring Reddit Clone app. " +
                            "Please click on the below url to activate your account : " +
                            "http://localhost:8080/api/auth/accountVerification/" + token ) );
        }
        catch( MailException e )
        {
            Logger.getLogger( getClass().getName() ).log( Level.SEVERE, "Activation mail not", e );
            throw new SpringRedditCloneException( "Activation mail not : " + e.getMessage() );
        }
    }

    private String generateVerificationToken( User user )
    {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken( token );
        verificationToken.setUser( user );

        verificationTokenRepository.save( verificationToken );
        return token;
    }

    public void verifyAccount( String token )
    {
        try
        {
            Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken( token );
            verificationToken.orElseThrow( () -> new SpringRedditCloneException( "Invalid token" ) );

            fetchUserAndEnable( verificationToken.get() );
        }
        catch( SpringRedditCloneException e )
        {
            throw new SpringRedditCloneException( e.getMessage() );
        }
    }

    @Transactional
    void fetchUserAndEnable( VerificationToken verificationToken )
    {
        String username = verificationToken.getUser().getUsername();
        Optional<User> userOptional = userRepository.findByUsername( username );
        userOptional.orElseThrow( () -> new SpringRedditCloneException( "User not found with name : " + username ) );

        User user = userOptional.get();
        user.setEnabled( true );
        userRepository.save( user );
    }

    public AuthenticationResponse login( LoginRequest loginRequest )
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken( loginRequest.getUsername(), loginRequest.getPassword() );
        Authentication authenticate = authenticationManager.authenticate( usernamePasswordAuthenticationToken );

        SecurityContextHolder.getContext().setAuthentication( authenticate );
        String jwtToken = jwtProvider.generateToken( authenticate );

        return AuthenticationResponse.builder()
                                     .authenticationToken( jwtToken )
                                     .refreshToken( refreshTokenService.generateRefreshToken().getToken() )
                                     .expiresAt( Instant.now().plusMillis( jwtProvider.getJwtExpirationInMillis() ) )
                                     .username( loginRequest.getUsername() )
                                     .build();
    }

    @Transactional( readOnly = true )
    public User getCurrentUser()
    {
        org.springframework.security.core.userdetails.User principal = ( org.springframework.security.core.userdetails.User ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername( principal.getUsername() )
                             .orElseThrow( () -> new UsernameNotFoundException( "User name not found - " + principal.getUsername() ) );
    }

    public AuthenticationResponse refreshToken( RefreshTokenRequest refreshTokenRequest )
    {
        refreshTokenService.validateRefreshToken( refreshTokenRequest.getRefreshToken() );
        String token = jwtProvider.generateTokenWithUsername( refreshTokenRequest.getUsername() );
        return AuthenticationResponse.builder()
                                     .authenticationToken( token )
                                     .refreshToken( refreshTokenRequest.getRefreshToken() )
                                     .expiresAt( Instant.now().plusMillis( jwtProvider.getJwtExpirationInMillis() ) )
                                     .username( refreshTokenRequest.getUsername() )
                                     .build();
    }
}
