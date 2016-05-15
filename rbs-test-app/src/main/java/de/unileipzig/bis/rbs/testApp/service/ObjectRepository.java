package de.unileipzig.bis.rbs.testApp.service;

import de.unileipzig.bis.rbs.testApp.model.Object;
import org.springframework.data.repository.CrudRepository;

/**
 * Object repository
 *
 * @author Stephan Kemper
 */
public interface ObjectRepository extends CrudRepository<Object, Long> {

}