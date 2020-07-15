package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.SubRedditDto;
import com.example.reddit.clone.model.Subreddit;
import com.example.reddit.clone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubRedditService
{
    private final SubredditRepository subredditRepository;

    @Transactional
    public SubRedditDto save( SubRedditDto subRedditDto )
    {
        Subreddit dto = mapSubRedditToDto( subRedditDto );
        Subreddit save = subredditRepository.save( dto );
        subRedditDto.setId( save.getId() );
        return subRedditDto;
    }

    private Subreddit mapSubRedditToDto( SubRedditDto subRedditDto )
    {
        return Subreddit.builder()
                        .name( subRedditDto.getName() )
                        .description( subRedditDto.getDescription() )
                        .build();
    }

    @Transactional( readOnly = true )
    public List<SubRedditDto> getAll()
    {
        return subredditRepository.findAll().stream().map( this::mapToDto ).collect( Collectors.toList() );
    }

    private SubRedditDto mapToDto( Subreddit subreddit )
    {
        return SubRedditDto.builder()
                           .name( subreddit.getName() )
                           .id( subreddit.getId() )
                           .description( subreddit.getDescription() )
                           .numberOfPosts( subreddit.getPosts().size() )
                           .build();
    }
}
