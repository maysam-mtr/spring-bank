package com.example.bankingApp.services;

import com.example.bankingApp.exceptions.ResourceNotFoundException;
import com.example.bankingApp.models.User;
import com.example.bankingApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing users.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * @param userRepository Repository for users.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     * @param user The user to create.
     * @return The created user.
     */
    public User createUser(User user){
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalStateException("username taken");
        }
        Long id = 0L;
        do {
            id = (long) (Math.random() * 9000000L) + 1000000L;
        } while (userRepository.existsById(id));
        user.setUserId(id);
        User savedUser = userRepository.save(user);
        if (savedUser == null || savedUser.getUserId() == null) {
            throw new IllegalStateException("User could not be saved");
        }
        return savedUser;

    }

    /**
     * Authenticates a user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param role The role of the user.
     * @return The authenticated user.
     */
    public User authenticateUser(String username, String password, int role){
        if (!username.isEmpty() && !password.isEmpty()) {
            Optional<User> user = userRepository.findOneForLogin(username, password, role);
            if (user.isPresent()){
                return user.get();
            }else{
                return new User();
            }
        }else{
            throw new IllegalArgumentException("Username and password cannot be empty");
        }
    }

    /**
     * Gets all users.
     * @return A list of all users.
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Gets all clients.
     * @return A list of all clients.
     */
    public List<User> getAllClients(){
        return userRepository.findAllClients();
    }

    /**
     * Finds a user by ID.
     * @param id The ID of the user.
     * @return The found user.
     */
    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    /**
     * Gets a user by username.
     * @param username The username of the user.
     * @return The found user.
     */
    public User getUserByUsername(String username){
        System.out.println(username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    /**
     * Checks if a user exists by ID.
     * @param id The ID of the user.
     * @return True if the user exists, false otherwise.
     */
    public boolean userExistsById(Long id){ return userRepository.existsById(id); }

    /**
     * Deletes a user.
     * @param user The user to delete.
     */
    public void deleteUser(User user){
        if(userExistsById(user.getUserId())) {
            userRepository.delete(user);
        }else {
            throw new ResourceNotFoundException("User does not exist");
        }
    }

    /**
     * Deletes a user by ID.
     * @param id The ID of the user.
     */
    public void deleteUserById(Long id){
        if (userExistsById(id)) {
            userRepository.deleteById(id);
        }else {
            throw new ResourceNotFoundException("User does not exist");
        }
    }

    /**
     * Updates a user.
     * @param id The ID of the user.
     * @param updates The updates to apply to the user.
     * @return The updated user.
     */
    @Transactional
    public User updateUser(Long id, Map<String, String> updates){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updates.containsKey("user_phone_nb")) {
            user.setPhoneNb((String) updates.get("user_phone_nb"));
        }
        if (updates.containsKey("user_country")) {
            user.setCountry((String) updates.get("user_country"));
        }
        if (updates.containsKey("user_city")) {
            user.setCountry((String) updates.get("user_city"));
        }

        return userRepository.save(user);
    }

    /**
     * Updates a user's role.
     * @param id The ID of the user.
     * @param role The new role of the user.
     * @return The updated user.
     */
    @Transactional
    public User updateUserRole(Long id, int role){
        if (role > 2 || role < -1){
            throw new IllegalArgumentException("Invalid role value");
        }
        User user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User does not exist with id : " + id));

        userRepository.updateUserRole(id, role);

        return userRepository.findById(id).orElse(null);
    }

    /**
     * Searches for a user.
     * @param searchTerm The term to search for.
     * @return A list of users that match the search term.
     */
    public List<User> searchForUser(String searchTerm) {
        Set<User> users = new HashSet<>();
        users.addAll(userRepository.findAllByUsername(searchTerm));
        users.addAll(userRepository.findAllByFirstName(searchTerm));
        users.addAll(userRepository.findAllByLastName(searchTerm));

        List<User> list = new ArrayList<User>();
        list.addAll(users);
        return list;
    }
}
