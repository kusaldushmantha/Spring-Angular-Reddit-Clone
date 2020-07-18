package com.example.reddit.clone.mapper;

import com.example.reddit.clone.dto.SubredditDto;
import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper( componentModel = "spring" )
public interface SubRedditMapper
{
    @Mapping( target = "numberOfPosts", expression = "java( mapPosts ( subreddit.getPosts() ) )" )
    SubredditDto mapSubredditToDto( Subreddit subreddit );

    default Integer mapPosts( List<Post> numberOfPosts )
    {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping( target = "posts", ignore = true )
    Subreddit mapDtoToSubreddit( SubredditDto subredditDto );
}
