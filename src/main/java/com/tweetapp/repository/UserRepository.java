package com.tweetapp.repository;

import com.tweetapp.model.TweetUser;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<TweetUser,String> {
    TweetUser findAllByLoginId(String LoginId);

}
