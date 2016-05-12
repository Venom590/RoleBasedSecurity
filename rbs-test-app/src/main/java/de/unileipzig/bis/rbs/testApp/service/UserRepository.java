package de.unileipzig.bis.rbs.testApp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import de.unileipzig.bis.rbs.testApp.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);

}