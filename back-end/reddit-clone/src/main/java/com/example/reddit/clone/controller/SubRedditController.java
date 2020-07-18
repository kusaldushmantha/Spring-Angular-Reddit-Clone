package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.SubredditDto;
import com.example.reddit.clone.service.SubRedditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/subreddit" )
@AllArgsConstructor
public class SubRedditController
{
    private final SubRedditService subRedditService;

    @PostMapping()
    public ResponseEntity<SubredditDto> createSubreddit( @RequestBody SubredditDto subRedditDto )
    {
        SubredditDto save = subRedditService.save( subRedditDto );
        return ResponseEntity.status( HttpStatus.CREATED ).body( save );
    }

    @GetMapping()
    public ResponseEntity<List<SubredditDto>> getAllSubReddits()
    {
        List<SubredditDto> all = subRedditService.getAll();
        return ResponseEntity.status( HttpStatus.OK ).body( all );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<SubredditDto> getSubReddit( @PathVariable Long id )
    {
        SubredditDto subReddit = subRedditService.getSubReddit( id );
        return ResponseEntity.status( HttpStatus.OK ).body( subReddit );
    }
}
