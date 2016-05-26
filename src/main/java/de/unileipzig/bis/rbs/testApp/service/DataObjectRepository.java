package de.unileipzig.bis.rbs.testApp.service;

import de.unileipzig.bis.rbs.testApp.model.DataObject;
import org.springframework.data.repository.CrudRepository;

/**
 * DataObject repository
 *
 * @author Stephan Kemper
 */
public interface DataObjectRepository extends CrudRepository<DataObject, Long> {

}