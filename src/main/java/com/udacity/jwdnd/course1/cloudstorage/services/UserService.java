package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public int createUser(User user) {
        SecureRandom sr = new SecureRandom();
        byte[] salt  = new byte[16];
        sr.nextBytes(salt);
        String encodedSalt  = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(
                user.getPassword(),
                encodedSalt);
        logger.debug("user with username = " + user.getUsername() + " was successfully created");
        return userMapper.insert(new User(
                null,
                user.getUsername(),
                encodedSalt,
                hashedPassword,
                user.getFirstName(),
                user.getLastName()
        ));
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }
}
