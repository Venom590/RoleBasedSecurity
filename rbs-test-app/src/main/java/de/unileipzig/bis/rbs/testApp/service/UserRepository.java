package de.unileipzig.bis.rbs.testApp.service;

import org.springframework.data.repository.CrudRepository;
import de.unileipzig.bis.rbs.testApp.model.User;

/**
 * User repository
 *
 * @author Lukas Werner
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Find a single user by the username
     *
     * @param username the username to search for
     * @return the matching user
     */
    User findByUsername(String username);

}