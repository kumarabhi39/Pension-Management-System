package com.cts.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.authorization.model.User;

/**
 * This class is for Repository for User Credentials
 * 
 * @author Abhishek Kumar
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
