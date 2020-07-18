package com.example.reddit.clone.controller;

import com.example.reddit.clone.dto.PostRequest;
import com.example.reddit.clone.dto.PostResponse;
import com.example.reddit.clone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api/posts" )
@AllArgsConstructor
public class PostController
{
    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost( @RequestBody PostRequest postRequest )
    {
        postService.save( postRequest );
        return new ResponseEntity( HttpStatus.CREATED );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<PostResponse> getPosts( @PathVariable Long id )
    {
        PostResponse postResponse = postService.getPost( id );
        return new ResponseEntity<>( postResponse, HttpStatus.OK );
    }

    @GetMapping()
    public ResponseEntity<List<PostResponse>> getAllPosts()
    {
        List<PostResponse> postResponseList = postService.getAllPosts();
        return new ResponseEntity<>( postResponseList, HttpStatus.OK );
    }

    @GetMapping( "/by-subreddit/{id}" )
    public ResponseEntity<List<PostResponse>> getAllPosts( @PathVariable Long id )
    {
        List<PostResponse> postResponseList = postService.getPostsBySubreddit( id );
        return new ResponseEntity<>( postResponseList, HttpStatus.OK );
    }

    @GetMapping( "/by-user/{name}" )
    public ResponseEntity<List<PostResponse>> getAllPosts( @PathVariable String name )
    {
        List<PostResponse> postResponseList = postService.getPostsByUsername( name );
        return new ResponseEntity<>( postResponseList, HttpStatus.OK );
    }
}
