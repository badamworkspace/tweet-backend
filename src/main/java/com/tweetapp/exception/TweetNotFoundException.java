package com.tweetapp.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class TweetNotFoundException extends RuntimeException{
    public TweetNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
