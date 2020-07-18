package com.example.reddit.clone.service;

import com.example.reddit.clone.dto.SubredditDto;
import com.example.reddit.clone.exception.SpringRedditCloneException;
import com.example.reddit.clone.mapper.SubRedditMapper;
import com.example.reddit.clone.model.Subreddit;
import com.example.reddit.clone.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubRedditService
{
    private final SubRedditRepository subRedditRepository;
    private final SubRedditMapper subRedditMapper;

    @Transactional
    public SubredditDto save( SubredditDto subRedditDto )
    {
        Subreddit dto = subRedditMapper.mapDtoToSubreddit( subRedditDto );
        Subreddit save = subRedditRepository.save( dto );
        subRedditDto.setId( save.getId() );
        return subRedditDto;
    }


    @Transactional( readOnly = true )
    public List<SubredditDto> getAll()
    {
        return subRedditRepository.findAll()
                                  .stream().map( subRedditMapper::mapSubredditToDto )
                                  .collect( Collectors.toList() );
    }

    @Transactional( readOnly = true )
    public SubredditDto getSubReddit( Long id )
    {
        Subreddit subReddit = subRedditRepository.findById( id )
                                                 .orElseThrow( () -> new SpringRedditCloneException( "No subreddit found for id : " + id ) );
        return subRedditMapper.mapSubredditToDto( subReddit );
    }
}
