package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.PostRequest;
import com.example.reddit.clone.dto.PostResponse;
import com.example.reddit.clone.exception.PostNotFoundException;
import com.example.reddit.clone.exception.SubRedditNotFoundException;
import com.example.reddit.clone.mapper.PostMapper;
import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.Subreddit;
import com.example.reddit.clone.model.User;
import com.example.reddit.clone.repository.PostRepository;
import com.example.reddit.clone.repository.SubRedditRepository;
import com.example.reddit.clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PostService
{
    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Transactional
    public void save( PostRequest postRequest )
    {
        Subreddit subReddit = subRedditRepository.findByName( postRequest.getSubredditName() )
                                                 .orElseThrow( () -> new SubRedditNotFoundException( "No subreddit found for name : " + postRequest.getSubredditName() ) );
        User currentUser = authService.getCurrentUser();
        Post mappedPost = postMapper.map( postRequest, subReddit, currentUser );
        postRepository.save( mappedPost );
    }

    @Transactional( readOnly = true )
    public PostResponse getPost( Long id )
    {
        Post post = postRepository.findById( id )
                                  .orElseThrow( () -> new PostNotFoundException( id.toString() ) );
        return postMapper.mapToDto( post );
    }

    @Transactional( readOnly = true )
    public List<PostResponse> getAllPosts()
    {
        return postRepository.findAll()
                             .stream()
                             .map( postMapper::mapToDto )
                             .collect( toList() );
    }

    @Transactional( readOnly = true )
    public List<PostResponse> getPostsBySubreddit( Long subredditId )
    {
        Subreddit subreddit = subRedditRepository.findById( subredditId )
                                                 .orElseThrow( () -> new SubRedditNotFoundException( subredditId.toString() ) );
        List<Post> posts = postRepository.findAllBySubreddit( subreddit );
        return posts.stream().map( postMapper::mapToDto ).collect( toList() );
    }

    @Transactional( readOnly = true )
    public List<PostResponse> getPostsByUsername( String username )
    {
        User user = userRepository.findByUsername( username )
                                  .orElseThrow( () -> new UsernameNotFoundException( username ) );
        return postRepository.findByUser( user )
                             .stream()
                             .map( postMapper::mapToDto )
                             .collect( toList() );
    }
}
