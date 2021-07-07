package com.user.management.controller;

import java.io.IOException;
import java.util.UUID;

import com.user.management.Models.CompleteUsers;
import com.user.management.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * This method returns the complied user data using the provided id. 
     * @param id
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompleteUsers> getUser(@PathVariable UUID id) throws IOException, InterruptedException{
        return ResponseEntity.ok().body(userService.getById(id));
    }

    /**
     * This method creates a user and address data 
     * @param newUser
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<UUID> saveUser(@RequestBody CompleteUsers newUser) throws Exception {
        return ResponseEntity.ok().body(userService.save(newUser));
    }
}