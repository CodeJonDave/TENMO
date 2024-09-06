package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.InvalidUserIdException;
import com.techelevator.tenmo.exception.InvalidUsernameException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public List<User> getUsers() throws UserNotFoundException {
        List<User> users = userDao.getUsers();
        if (users.isEmpty()){
            throw new UserNotFoundException("There were no users found");
        }
        return users;
    }

    public User getUser(int userId) throws InvalidUserIdException, UserNotFoundException {
       if(userId <= 0){
           throw new InvalidUserIdException("Invalid user ID: " +userId);
       }
      return Optional.ofNullable(userDao.getUserById(userId))
              .orElseThrow(()-> new UserNotFoundException("User not found for Id: "+userId));
    }

    public User getUserByUsername(String username) throws InvalidUsernameException, UserNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException("The username cannot be null or empty.");
        }
        return Optional.ofNullable(userDao.getUserByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("User not found for Username: "+username));
    }
}
