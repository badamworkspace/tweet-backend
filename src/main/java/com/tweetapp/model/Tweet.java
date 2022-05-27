package com.tweetapp.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tweet {

    @Id
    private UUID tweetId;

    @NotBlank(message = "message should not be null")
    @Max(value = 144,message = "message should be less than 144 characters")
    private String message;

    @Min(0)
    private long likes;

    @NotBlank(message = "username should not be null")
    private String username;

    private LocalDateTime createdDateTime;

    private List<Reply> replyMessage;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Reply {
        @NotBlank(message = "message should not be null")
        @Max(value = 144,message = "message should be less than 144 characters")
        private String message;

        @NotBlank(message = "username should not be null")
        private String username;

        private LocalDateTime createdDate;

    }
}
