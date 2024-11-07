package com.example.bankingApp.controllers;

import com.example.bankingApp.models.DebitCard;
import com.example.bankingApp.services.AccountService;
import com.example.bankingApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bankingApp.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for managing users.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController.
     * @param userService Service for users.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets all users.
     * @return A list of all users.
     */
    @GetMapping("getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> clients = userService.getAllUsers();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    /**
     * Gets all clients.
     * @return A list of all clients.
     */
    @GetMapping("getAllClients")
    public ResponseEntity<List<User>> getAllClients(){
        List<User> clients = userService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    /**
     * Gets a user by their ID.
     * @param id The ID of the user.
     * @return The user.
     */
    @GetMapping("getUserById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long id){
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Gets a user by their username.
     * @param username The username of the user.
     * @return The user.
     */
    @GetMapping("getUserByUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username){
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    /**
     * Searches for a user.
     * @param searchTerm The search term.
     * @return A list of users that match the search term.
     */
    @GetMapping("searchForUser/{searchItem}")
    public ResponseEntity<List<User>> searchForUser(@PathVariable("searchItem") String searchTerm){
        return new ResponseEntity<List<User>>(userService.searchForUser(searchTerm), HttpStatus.OK);
    }

    /**
     * Authenticates a user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param role The role of the user.
     * @return The authenticated user.
     */
    @GetMapping("authenticateUser/{username}/{password}/{role}")
    @ResponseBody
    public ResponseEntity<User> authenticateUser(@PathVariable("username") String username,
                                                 @PathVariable("password") String password,
                                                 @PathVariable("role") int role){
        User user = userService.authenticateUser(username, password, role);
        if (user.getUsername() == null){
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Creates a user.
     * @param user The user to create.
     * @return The created user.
     */
    @PostMapping("createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userService.createUser(user);
        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Updates the role of a user.
     * @param id The ID of the user.
     * @param role The new role of the user.
     * @return The updated user.
     */
    @PutMapping("updateUserRole/{id}/{role}")
    public ResponseEntity<User> updateUserRole(@PathVariable("id") Long id, @PathVariable("role") int role){
        return new ResponseEntity<User>(userService.updateUserRole(id, role), HttpStatus.OK);
    }

    /**
     * Updates a user.
     * @param id The ID of the user.
     * @param updates The updates to apply to the user.
     * @return The updated user.
     */
    @PutMapping("updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody Map<String, String> updates){
        return new ResponseEntity<User>(userService.updateUser(id, updates), HttpStatus.OK);
    }

    /**
     * Deletes a user.
     * @param id The ID of the user.
     */
    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}