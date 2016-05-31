package de.unileipzig.bis.rbs.testApp.service;

import de.unileipzig.bis.rbs.testApp.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Role repository
 *
 * @author Stephan Kemper
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Find a single role by the name
     *
     * @param name the name to search for
     * @return the matching role
     */
    Role findByName(String name);

}