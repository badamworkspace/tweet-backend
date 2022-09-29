package com.tweetapp.kafka;


import com.tweetapp.controller.TweetController;
import com.tweetapp.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService{


//    @KafkaListener(topics = "notification" , groupId ="TweetApp" )
//    public void consume(String message)
//    {
//        log.info(String.format("New Tweet By -> %s", message));
//        TweetController.messages.add(String.format("New Tweet By -> %s", message));
//        //log.info(String.valueOf(TweetController.messages.size()));
//    }
}
