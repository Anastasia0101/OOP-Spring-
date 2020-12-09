package com.oop.lab.first.firstlab.controller;

import com.oop.lab.first.firstlab.entity.User;
import com.oop.lab.first.firstlab.exception.ResourceNotFoundException;
import com.oop.lab.first.firstlab.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class UserController {
 
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        
        private final int ROW_PER_PAGE = 5;
        
        @Autowired
        private UserService userService;
        
        @GetMapping(value = "/users")
        public String getUsers(Model model,
                @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
                List<User> users = userService.findAll(pageNumber, ROW_PER_PAGE);
        
        long count = userService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("users", users);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "users-list";
        }

        @GetMapping(value = "/users/{userId}")
        public String getUserById(Model model, @PathVariable long userId) {
        User user = null;
        try {
                user = userService.findById(userId);
        } catch (ResourceNotFoundException ex) {
                model.addAttribute("errorMessage", "User not found");
        }
        model.addAttribute("user", user);
        return "user";
        }

        @GetMapping(value = {"/users/add"})
        public String showAddUser(Model model) {
                User user = new User();
                model.addAttribute("add", true);
                model.addAttribute("user", user);
        
                return "user-edit";
        }
        
        @PostMapping(value = "/users/add")
        public String addUser(Model model,
                @ModelAttribute("user") User user) {        
                try {
                User newUser = userService.save(user);
                return "redirect:/users/" + String.valueOf(newUser.getId());
                } catch (Exception ex) {
                String errorMessage = ex.getMessage();
                logger.error(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
        
                model.addAttribute("add", true);
                return "user-edit";
                }        
        }

        @GetMapping(value = {"/users/{userId}/edit"})
        public String showEditUser(Model model, @PathVariable long userId) {
                User user = null;
                try {
                user = userService.findById(userId);
                } catch (ResourceNotFoundException ex) {
                model.addAttribute("errorMessage", "User not found");
                }
                model.addAttribute("add", false);
                model.addAttribute("user", user);
                return "user-edit";
        }
        
        @PostMapping(value = {"/users/{userId}/edit"})
        public String updateUser(Model model,
                @PathVariable long userId,
                @ModelAttribute("user") User user) {        
                try {
                user.setId(userId);
                userService.update(user);
                return "redirect:/users/" + String.valueOf(user.getId());
                } catch (Exception ex) {
                String errorMessage = ex.getMessage();
                logger.error(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
        
                model.addAttribute("add", false);
                return "user-edit";
                }
        }
        
        @GetMapping(value = {"/users/{userId}/delete"})
        public String showDeleteUserById(
                Model model, @PathVariable long userId) {
                User user = null;
            try {
                user = userService.findById(userId);
            } catch (ResourceNotFoundException ex) {
                model.addAttribute("errorMessage", "User not found");
            }
            model.addAttribute("allowDelete", true);
            model.addAttribute("user", user);
            return "user";
        }
         
        @PostMapping(value = {"/users/{userId}/delete"})
        public String deleteUserById(
                Model model, @PathVariable long userId) {
            try {
                userService.deleteById(userId);
                return "redirect:/users";
            } catch (ResourceNotFoundException ex) {
                String errorMessage = ex.getMessage();
                logger.error(errorMessage);
                model.addAttribute("errorMessage", errorMessage);
                return "user";
            }
        }
}