package com.example.reddit.clone.service;

import com.example.reddit.clone.exception.SpringRedditCloneException;
import com.example.reddit.clone.model.RefreshToken;
import com.example.reddit.clone.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService
{
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken generateRefreshToken()
    {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken( UUID.randomUUID().toString() );
        refreshToken.setCreatedDate( Instant.now() );

        return refreshTokenRepository.save( refreshToken );
    }

    @Transactional( readOnly = true )
    public void validateRefreshToken( String token )
    {
        refreshTokenRepository.findByToken( token )
                              .orElseThrow( () -> new SpringRedditCloneException( "Invalid refresh token" ) );
    }

    @Transactional(  )
    public void deleteRefreshToken( String token )
    {
        refreshTokenRepository.deleteByToken( token );
    }
}
