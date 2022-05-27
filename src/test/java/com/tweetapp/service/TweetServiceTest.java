package com.tweetapp.service;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.kafka.KafkaProducerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.repository.TweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TweetService underTest;

    Tweet tweet = Tweet.builder().message("Hi").build();
    String username = "Bhanuteja";
    @Test
    void add() {
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);
        Tweet result = underTest.add(tweet, username);
        assertNotNull(result.getTweetId());
        assertEquals(username,result.getUsername());
    }

    @Test
    void update() throws TweetNotFoundException {
        tweet.setTweetId(UUID.randomUUID());
        tweet.setUsername(username);
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);
        when(tweetRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(tweet));
        Tweet result = underTest.update(tweet,tweet.getTweetId(), username);
        assertNotNull(result.getTweetId());
        assertEquals(username,result.getUsername());
    }
    @Test
    void updateThrowsError() throws TweetNotFoundException {
        tweet.setTweetId(UUID.randomUUID());
        tweet.setUsername(username);
        //when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);
        when(tweetRepository.findById(any(UUID.class))).thenThrow(TweetNotFoundException.class);
        assertThrows(TweetNotFoundException.class,() -> underTest.update(tweet,tweet.getTweetId(), username));
    }

}