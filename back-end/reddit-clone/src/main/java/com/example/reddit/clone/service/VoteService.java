package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.VoteDto;
import com.example.reddit.clone.exception.PostNotFoundException;
import com.example.reddit.clone.exception.SpringRedditCloneException;
import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.Vote;
import com.example.reddit.clone.model.VoteType;
import com.example.reddit.clone.repository.PostRepository;
import com.example.reddit.clone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService
{
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote( VoteDto voteDto )
    {
        Post post = postRepository.findById( voteDto.getPostId() )
                                  .orElseThrow( () -> new PostNotFoundException( "Post not found for ID : " + voteDto.getPostId() ) );
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc( post, authService.getCurrentUser() );
        if( voteByPostAndUser.isPresent()
                    && voteByPostAndUser.get().getVoteType().equals( voteDto.getVoteType() ) )
        {
            throw new SpringRedditCloneException( "You have already " + voteDto.getVoteType() + " this post" );
        }

        if( VoteType.UPVOTE.equals( voteDto.getVoteType() ) )
        {
            post.setVoteCount( post.getVoteCount() + 1 );
        }
        else
        {
            post.setVoteCount( post.getVoteCount() - 1 );
        }
        voteRepository.save( mapToVote( voteDto, post ) );
        postRepository.save( post );
    }

    private Vote mapToVote( VoteDto voteDto, Post post )
    {
        return Vote.builder()
                   .voteType( voteDto.getVoteType() )
                   .post( post )
                   .user( authService.getCurrentUser() )
                   .build();
    }


}
