package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TweetRepository extends MongoRepository<Tweet, UUID> {
    Optional<List<Tweet>> findAllByUsername(String username);
}
