package com.tweetapp.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class UserAlreadyExists extends Exception {
    public UserAlreadyExists(String errorMessage) {
        super(errorMessage);
    }

}
