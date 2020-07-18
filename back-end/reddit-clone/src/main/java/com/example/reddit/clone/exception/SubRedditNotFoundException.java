package com.example.reddit.clone.exception;

public class SubRedditNotFoundException extends RuntimeException
{
    public SubRedditNotFoundException( String message )
    {
        super( message );
    }
}
