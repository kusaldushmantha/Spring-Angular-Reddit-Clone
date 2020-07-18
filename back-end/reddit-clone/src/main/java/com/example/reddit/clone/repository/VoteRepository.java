package com.example.reddit.clone.repository;

import com.example.reddit.clone.model.Post;
import com.example.reddit.clone.model.User;
import com.example.reddit.clone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long>
{
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc( Post post, User currentUser );
}
