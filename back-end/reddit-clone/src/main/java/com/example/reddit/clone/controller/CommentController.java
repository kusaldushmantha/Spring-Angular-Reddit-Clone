package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.CommentDto;
import com.example.reddit.clone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping( "/api/comment" )
@AllArgsConstructor
public class CommentController
{
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity createComment( @RequestBody CommentDto commentDto )
    {
        commentService.save( commentDto );
        return new ResponseEntity<>( HttpStatus.CREATED );
    }

    @GetMapping( "/by-post/{postId}" )
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost( @PathVariable Long postId )
    {
        return ResponseEntity.status( OK )
                             .body( commentService.getAllCommentsForPost( postId ) );
    }

    @GetMapping( "/by-user/{userName}" )
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser( @PathVariable String userName )
    {
        return ResponseEntity.status( OK )
                             .body( commentService.getAllCommentsForUser( userName ) );
    }
}
