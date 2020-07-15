package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.AuthenticationResponse;
import com.example.reddit.clone.dto.LoginRequest;
import com.example.reddit.clone.dto.RegisterRequest;
import com.example.reddit.clone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/auth" )
@AllArgsConstructor
public class AuthController
{
    private final AuthService authService;

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
    public ResponseEntity login( @RequestBody LoginRequest loginRequest )
    {
        try
        {
            AuthenticationResponse authenticationResponse = authService.login( loginRequest );
            return new ResponseEntity<>( authenticationResponse, HttpStatus.OK );
        }
        catch( Exception e )
        {
            return new ResponseEntity<>( "User authentication failed : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }
}
