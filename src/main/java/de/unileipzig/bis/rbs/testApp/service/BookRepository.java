package de.unileipzig.bis.rbs.testApp.service;

import de.unileipzig.bis.rbs.testApp.model.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Book repository
 *
 * @author Lukas Werner
 */
public interface BookRepository extends CrudRepository<Book, Long> {



}