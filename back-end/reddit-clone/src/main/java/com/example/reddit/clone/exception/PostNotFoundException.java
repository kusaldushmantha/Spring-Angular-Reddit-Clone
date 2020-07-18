package com.example.reddit.clone.exception;

public class PostNotFoundException extends RuntimeException
{
    public PostNotFoundException( String s )
    {
        super( s );
    }
}
