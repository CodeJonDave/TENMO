package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.InvalidUserIdException;
import com.techelevator.tenmo.exception.InvalidUsernameException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "user/")
@Validated
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "")
    public ResponseEntity<List<User>> getUsers()
            throws UserNotFoundException {

        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "{userId}")
    public ResponseEntity<User> getUserByID(@PathVariable("userId") int userId)
            throws UserNotFoundException,
            InvalidUserIdException {

        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "by-username")
    public ResponseEntity<User> getUserByUserName(@RequestParam @Valid String username)
            throws UserNotFoundException,
            InvalidUsernameException {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}

