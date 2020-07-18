package com.example.reddit.clone;

import com.example.reddit.clone.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import( SwaggerConfig.class )
public class RedditCloneApplication
{

    public static void main( String[] args )
    {
        SpringApplication.run( RedditCloneApplication.class, args );
    }

}
