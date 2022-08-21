package com.cts.processPension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.processPension.model.PensionerInput;

/**
 * This Repository is for Pensioner Input which we give as input in this
 * microservice
 * 
 * @author Abhishek Kumar
 *
 */

@Repository
public interface PensionerInputRepository extends JpaRepository<PensionerInput, String> {

}
