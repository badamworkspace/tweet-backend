package com.tweetapp.service;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.kafka.KafkaProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TweetService {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;
    public Tweet add(Tweet tweet, String username) {
        tweet.setTweetId(UUID.randomUUID());
        tweet.setUsername(username);
        tweet.setCreatedDateTime(LocalDateTime.now());
        tweet.setLikes(0);
        log.info("Tweet with id:{} posted by {}",tweet.getTweetId(),username);
        kafkaProducerService.sendMessage(tweet.getUsername());
        return tweetRepository.save(tweet);
    }

    public Tweet update(Tweet updateTweet, UUID tweetId,String username) throws TweetNotFoundException {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if(tweet.isPresent()) {
            tweet.get().setMessage(updateTweet.getMessage());
            log.info("Tweet with id:{} updated by {}", tweet.get().getTweetId(), username);
            return tweetRepository.save(tweet.get());
        }else throw new TweetNotFoundException("Tweet with id : "+tweetId+" not found");
    }

    public String delete(String username, UUID tweetId) throws TweetNotFoundException {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if(tweet.isPresent() && tweet.get().getUsername().equals(username)) {
            log.info("Tweet with id:{} deleted by {}", tweet.get().getTweetId(), tweet.get().getUsername());
            tweetRepository.delete(tweet.get());
            return "Tweet deleted";
        }else throw new TweetNotFoundException("Tweet with id : "+tweetId+" not found");
    }

    public Tweet likes(String username, UUID tweetId) throws TweetNotFoundException {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if(tweet.isPresent()) {
            tweet.get().setLikes(tweet.get().getLikes()+1);
            log.info("Tweet with id:{} liked by {}", tweet.get().getTweetId(), username);
            return tweetRepository.save(tweet.get());
        }else throw new TweetNotFoundException("Tweet with id : "+tweetId+" not found");
    }

    public Tweet reply(Tweet.Reply tweetReply, UUID tweetId, String username) throws TweetNotFoundException {
        Optional<Tweet> tweet = tweetRepository.findById(tweetId);
        if(tweet.isPresent()) {
            tweetReply.setUsername(username);
            tweetReply.setCreatedDate(LocalDateTime.now());
            List<Tweet.Reply> updateReply = tweet.get().getReplyMessage();
            updateReply.add(tweetReply);
            tweet.get().setReplyMessage(updateReply);
            log.info("Tweet with id:{} replied by {}", tweet.get().getTweetId(), username);
            return tweetRepository.save(tweet.get());
        }else throw new TweetNotFoundException("Tweet with id : "+tweetId+" not found");
    }

    public List<Tweet> getAllTweetsByUser(String username) throws TweetNotFoundException {
        Optional<List<Tweet>> result = tweetRepository.findAllByUsername(username);
        if(!result.get().isEmpty()){
            return result.get();
        }else throw new TweetNotFoundException("Tweets not found for user : "+username);

    }

    public List<Tweet> getAllTweets() {
        return tweetRepository.findAll();
    }
}
