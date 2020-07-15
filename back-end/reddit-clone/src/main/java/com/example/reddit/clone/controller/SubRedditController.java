package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.SubRedditDto;
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
    public ResponseEntity<SubRedditDto> createSubreddit( @RequestBody SubRedditDto subRedditDto )
    {
        SubRedditDto save = subRedditService.save( subRedditDto );
        return ResponseEntity.status( HttpStatus.CREATED ).body( save );
    }

    @GetMapping()
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits()
    {
        List<SubRedditDto> all = subRedditService.getAll();
        return ResponseEntity.status( HttpStatus.OK ).body( all );
    }
}
