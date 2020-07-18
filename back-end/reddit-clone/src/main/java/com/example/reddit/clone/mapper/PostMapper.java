package com.example.reddit.clone.mapper;

import com.example.reddit.clone.dto.PostRequest;
import com.example.reddit.clone.dto.PostResponse;
import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.Subreddit;
import com.example.reddit.clone.model.User;
import com.example.reddit.clone.repository.CommentRepository;
import com.example.reddit.clone.repository.VoteRepository;
import com.example.reddit.clone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

@Mapper( componentModel = "spring" )
public abstract class PostMapper
{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Mapping( target = "createdDate", expression = "java( java.time.Instant.now() )" )
    @Mapping( target = "description", source = "postRequest.description" )
    @Mapping( target = "subreddit", source = "subreddit" )
    @Mapping( target = "voteCount", constant = "0" )
    @Mapping( target = "user", source = "user" )
    public abstract Post map( PostRequest postRequest, Subreddit subreddit, User user );

    @Mapping( target = "id", source = "postId" )
    @Mapping( target = "subredditName", source = "subreddit.name" )
    @Mapping( target = "userName", source = "user.username" )
    @Mapping( target = "commentCount", expression = "java( commentCount( post ) )" )
    @Mapping( target = "duration", expression = "java( getDuration( post ) )" )
    public abstract PostResponse mapToDto( Post post );

    Integer commentCount( Post post )
    {
        return commentRepository.findByPost( post ).size();
    }

    String getDuration( Post post )
    {
        return TimeAgo.using( post.getCreatedDate().toEpochMilli() );
    }
}
