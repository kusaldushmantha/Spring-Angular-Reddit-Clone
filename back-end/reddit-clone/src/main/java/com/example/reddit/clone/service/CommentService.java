package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.CommentDto;
import com.example.reddit.clone.exception.PostNotFoundException;
import com.example.reddit.clone.mapper.CommentMapper;
import com.example.reddit.clone.model.Comment;
import com.example.reddit.clone.model.NotificationMail;
import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.User;
import com.example.reddit.clone.repository.CommentRepository;
import com.example.reddit.clone.repository.PostRepository;
import com.example.reddit.clone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService
{
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional
    public void save( CommentDto commentDto )
    {
        Post post = postRepository.findById( commentDto.getPostId() )
                                  .orElseThrow( () -> new PostNotFoundException( "Post not found for ID : " + commentDto.getId() ) );

        Comment comment = commentMapper.map( commentDto, post, authService.getCurrentUser() );
        commentRepository.save( comment );

        String message = post.getUser().getUsername() + " commented on your post " + post.getUrl();
        sendCommentNotification( message, post.getUser() );
    }

    private void sendCommentNotification( String message, User user )
    {
        mailService.sendMail( new NotificationMail( user.getUsername() + " Commented on your post", user.getEmail(), message ) );
    }

    @Transactional( readOnly = true )
    public List<CommentDto> getAllCommentsForPost( Long postId )
    {
        Post post = postRepository.findById( postId ).orElseThrow( () -> new PostNotFoundException( postId.toString() ) );
        return commentRepository.findByPost( post )
                                .stream()
                                .map( commentMapper::mapToDto ).collect( toList() );
    }

    @Transactional( readOnly = true )
    public List<CommentDto> getAllCommentsForUser( String userName )
    {
        User user = userRepository.findByUsername( userName )
                                  .orElseThrow( () -> new UsernameNotFoundException( userName ) );
        return commentRepository.findAllByUser( user )
                                .stream()
                                .map( commentMapper::mapToDto )
                                .collect( toList() );
    }
}
