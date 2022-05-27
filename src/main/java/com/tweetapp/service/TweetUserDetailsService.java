package com.tweetapp.service;

import com.tweetapp.exception.UserAlreadyExists;
import com.tweetapp.model.JwtRequest;
import com.tweetapp.model.TweetUser;
import com.tweetapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MongoTemplate mongoTemplate;

    public TweetUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Retrieving TweetUser {} data from database",username);
         TweetUser tweetUser= userRepository.findAllByLoginId(username);
        if (tweetUser!=null) {
            log.info("Data from database {}",tweetUser.getLoginId());
            return new User(tweetUser.getLoginId(), tweetUser.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public TweetUser save(TweetUser tweetUser) throws UserAlreadyExists {
        List<TweetUser> allUsers = allUsers().stream().filter(user -> user.getLoginId().equals(tweetUser.getLoginId()) || user.getEmail().equals(tweetUser.getEmail()))
                .collect(Collectors.toList());
        if (allUsers.isEmpty()) {
            tweetUser.setPassword(passwordEncoder.encode(tweetUser.getPassword()));
            log.info("Saving user : {}", tweetUser.getLoginId());
            return userRepository.save(tweetUser);
        } else throw new UserAlreadyExists("User is already registered");
    }

    public TweetUser update(JwtRequest jwtRequest) {
        Optional<TweetUser> tweetUser = userRepository.findById(jwtRequest.getUsername());
        if(tweetUser.isPresent()){
            tweetUser.get().setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
            log.info("Updating password of {}",jwtRequest.getUsername());
            return userRepository.save(tweetUser.get());
        }else {
            throw new UsernameNotFoundException("User not found with username: " + jwtRequest.getUsername());
        }

    }

    public List<TweetUser> allUsers() {
        log.info("Retreiving users");
        return userRepository.findAll();
    }

    public List<TweetUser> allUsersByUsername(String username) {
       // Query query = new Query(Criteria.where("firstName").regex("^"+username));
        //List<TweetUser> result = mongoTemplate.find(query, TweetUser.class);
        List<TweetUser> result = allUsers();
        return result.stream().filter(user -> {
            String tweetUsername = user.getFirstName();
            return tweetUsername.equalsIgnoreCase(username);
        }).collect(Collectors.toList());
    }
}
