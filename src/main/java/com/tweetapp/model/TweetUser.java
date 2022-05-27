package com.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class TweetUser {
    @Id
    @NotBlank(message = "LoginId should not be blank")
    private String loginId;
    @NotBlank(message = "First name should not be blank")
    private String firstName;
    @NotBlank(message = "Last name should not be blank")
    private String lastName;
    @NotBlank(message = "Password should not be blank")
    private String password;
    @NotBlank(message = "Email should not be blank")
    private String email;
    @NotBlank(message = "Contact number should not be blank")
    private String contactNumber;


}
