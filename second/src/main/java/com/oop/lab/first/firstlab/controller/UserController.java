package com.oop.lab.first.firstlab.controller;

import com.oop.lab.first.firstlab.model.User;
import com.oop.lab.first.firstlab.service.UserService;
import com.oop.lab.first.firstlab.exception.ResourceNotFoundException;
import com.oop.lab.first.firstlab.exception.BadResourceException;
import com.oop.lab.first.firstlab.exception.ResourceAlreadyExistsException;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping
public class UserController {
 
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        
        private final int ROW_PER_PAGE = 5;
        
        @Autowired
        private UserService userService;
        
        @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<User>> findAll(
                @RequestParam(value="page", defaultValue="1") int pageNumber,
                @RequestParam(required=false) String name) {
            if (StringUtils.isEmpty(name)) {
                return ResponseEntity.ok(userService.findAll(pageNumber, ROW_PER_PAGE));
            }
            else {
                return ResponseEntity.ok(userService.findAllByName(name, pageNumber, ROW_PER_PAGE));
            }
        }

        @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<User> findUserById(@PathVariable long userId) {
                try {
                    User book = userService.findById(userId);
                    return ResponseEntity.ok(book); 
                } catch (ResourceNotFoundException ex) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // return 404, with null body
                }
        }

        @PostMapping(value = "/users")
        public ResponseEntity<User> addUser(@RequestBody User user) 
                throws URISyntaxException {
                try {
                    User newUser = userService.save(user);
                    return ResponseEntity.created(new URI("/users/" + newUser.getId()))
                            .body(user);
                } catch (ResourceAlreadyExistsException ex) {
                    // log exception first, then return Conflict (409)
                    logger.error(ex.getMessage());
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                } catch (BadResourceException ex) {
                    // log exception first, then return Bad Request (400)
                    logger.error(ex.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
        }
            
        @PutMapping(value = "/users/{userId}")
        public ResponseEntity<User> updateUser(@RequestBody User user, 
                @PathVariable long userId) {
                try {
                    user.setId(userId);
                    userService.update(user);
                    return ResponseEntity.ok().build();
                } catch (ResourceNotFoundException ex) {
                    // log exception first, then return Not Found (404)
                    logger.error(ex.getMessage());
                    return ResponseEntity.notFound().build();
                } catch (BadResourceException ex) {
                    // log exception first, then return Bad Request (400)
                    logger.error(ex.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
        }
                 
        @DeleteMapping(path="/users/{userId}")
        public ResponseEntity<Void> deleteUserById(@PathVariable long userId) {
                try {
                    userService.deleteById(userId);
                    return ResponseEntity.ok().build();
                } catch (ResourceNotFoundException ex) {
                    logger.error(ex.getMessage());
                    return ResponseEntity.notFound().build();
                }
        }
}