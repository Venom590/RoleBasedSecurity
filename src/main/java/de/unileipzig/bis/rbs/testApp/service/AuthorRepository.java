package de.unileipzig.bis.rbs.testApp.service;

import de.unileipzig.bis.rbs.testApp.model.Author;
import de.unileipzig.bis.rbs.testApp.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User repository
 *
 * @author Lukas Werner
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {

}