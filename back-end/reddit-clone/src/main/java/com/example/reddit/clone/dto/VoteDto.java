package com.example.reddit.clone.dto;

import com.example.reddit.clone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto
{
    private VoteType voteType;
    private Long postId;
}
