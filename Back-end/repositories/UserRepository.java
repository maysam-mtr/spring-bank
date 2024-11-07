package com.example.bankingApp.repositories;

import com.example.bankingApp.models.DebitCard;
import com.example.bankingApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * repository interface for user class
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * find all users with username
     * @param username
     * @return list of users
     */
    List<User> findAllByUsername(String username);

    /**
     * find all users by first name
     * @param firstName
     * @return list of users
     */
    List<User> findAllByFirstName(String firstName);

    /**
     * find all users by last name
     * @param lastName
     * @return list of users
     */
    List<User> findAllByLastName(String lastName);

    /**
     * find user by username
     * @param username
     * @return user object
     */
    Optional<User> findByUsername(String username);

    /**
     * find all clients
     * @return list of users
     */
    @Query(value = "SELECT * FROM users WHERE user_role = 1 OR user_role = 2", nativeQuery = true)
    List<User> findAllClients();

    /**
     * authenticate a user
     * @param username
     * @param password
     * @param role
     * @return
     */
    @Query(value = "SELECT * FROM users WHERE username = ?1 AND user_password = ?2 AND (user_role = ?3 OR user_role = 2)", nativeQuery = true)
    Optional<User> findOneForLogin(String username, String password, int role);

    /**
     * change user role
     * @param id
     * @param role
     */
    @Modifying
    @Query(value = "UPDATE users SET user_role = ?2 WHERE user_id = ?1", nativeQuery = true)
    void updateUserRole(Long id, int role);
}
