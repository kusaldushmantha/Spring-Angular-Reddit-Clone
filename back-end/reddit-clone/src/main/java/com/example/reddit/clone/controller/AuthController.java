package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.AuthenticationResponse;
import com.example.reddit.clone.dto.LoginRequest;
import com.example.reddit.clone.dto.RefreshTokenRequest;
import com.example.reddit.clone.dto.RegisterRequest;
import com.example.reddit.clone.service.AuthService;
import com.example.reddit.clone.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/api/auth" )
@AllArgsConstructor
public class AuthController
{
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping( "/signup" )
    public ResponseEntity<String> signUp( @RequestBody RegisterRequest registerRequest )
    {
        try
        {
            authService.signUp( registerRequest );
            return new ResponseEntity<>( "User registration successful", HttpStatus.OK );
        }
        catch( Exception e )
        {
            return new ResponseEntity<>( "User registration failed : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @GetMapping( "/accountVerification/{token}" )
    public ResponseEntity<String> verifyAccount( @PathVariable String token )
    {
        try
        {
            authService.verifyAccount( token );
            return new ResponseEntity<>( "User activation successful", HttpStatus.OK );
        }
        catch( Exception e )
        {
            return new ResponseEntity<>( "User activation failed : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @PostMapping( "/login" )
    public ResponseEntity<AuthenticationResponse> login( @RequestBody LoginRequest loginRequest )
    {
        AuthenticationResponse authenticationResponse = authService.login( loginRequest );
        return new ResponseEntity<>( authenticationResponse, HttpStatus.OK );
    }

    @PostMapping( "/refresh/token" )
    public ResponseEntity<AuthenticationResponse> refreshTokens( @Valid @RequestBody RefreshTokenRequest refreshTokenRequest )
    {
        AuthenticationResponse authenticationResponse = authService.refreshToken( refreshTokenRequest );
        return new ResponseEntity<>( authenticationResponse, HttpStatus.OK );
    }

    @PostMapping( "/logout" )
    public ResponseEntity<String> logout( @Valid @RequestBody RefreshTokenRequest refreshTokenRequest )
    {
        refreshTokenService.deleteRefreshToken( refreshTokenRequest.getRefreshToken() );
        return new ResponseEntity<>( "Refresh token deleted successfully", HttpStatus.OK );
    }
}
