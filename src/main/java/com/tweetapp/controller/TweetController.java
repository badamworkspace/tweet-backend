package com.tweetapp.controller;

import com.tweetapp.exception.TweetNotFoundException;
import com.tweetapp.kafka.KafkaConsumerService;
import com.tweetapp.model.Tweet;
import com.tweetapp.service.TweetService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1.0/tweets")
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "https://tweet-front.azurewebsites.net/")
public class TweetController {
    @Autowired
    private TweetService tweetService;
    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    public static List<String> messages = new ArrayList<>();

    @GetMapping("/{username}")
    public ResponseEntity<List<Tweet>> allTweetsOfUser(@PathVariable("username") String username) throws TweetNotFoundException {
        List<Tweet> result = tweetService.getAllTweetsByUser(username);
        return new ResponseEntity<List<Tweet>>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tweet>> allTweets() throws TweetNotFoundException {
        List<Tweet> result = tweetService.getAllTweets();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<Tweet> addTweet(@PathVariable("username") String username, @RequestBody Tweet tweet){
        Tweet result = tweetService.add(tweet, username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{username}/update/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable("username") String username, @RequestBody Tweet tweet,@PathVariable UUID id) throws TweetNotFoundException {
        Tweet result = tweetService.update(tweet,id,username);
        return new ResponseEntity<Tweet>(result, HttpStatus.OK);
    }

    @DeleteMapping ("/{username}/delete/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable("username") String username,@PathVariable UUID id) throws TweetNotFoundException {
        String result = tweetService.delete(username, id);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @PutMapping("/{username}/like/{id}")
    public ResponseEntity<Tweet> likeTweet(@PathVariable("username") String username,@PathVariable UUID id) throws TweetNotFoundException {
        Tweet result = tweetService.likes(username,id);
        return new ResponseEntity<Tweet>(result, HttpStatus.OK);
    }

//    @PutMapping("/{username}/unlike/{id}")
//    public ResponseEntity<Tweet> likeTweet(@PathVariable("username") String username,@PathVariable UUID id) throws TweetNotFoundException {
//        Tweet result = tweetService.likes(username,id);
//        return new ResponseEntity<Tweet>(result, HttpStatus.OK);
//    }

    @PostMapping("/{username}/reply/{id}")
    public ResponseEntity<Tweet> replyTweet(@PathVariable("username") String username, @RequestBody Tweet.Reply tweetReply,@PathVariable UUID id) throws TweetNotFoundException {
        Tweet result = tweetService.reply(tweetReply,id,username);
        return new ResponseEntity<Tweet>(result, HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity<List<String>> notifications() throws TweetNotFoundException {
        ResponseEntity<List<String>> result = new ResponseEntity<>(messages, HttpStatus.OK);
        //messages.clear();
        return result;
    }


}
