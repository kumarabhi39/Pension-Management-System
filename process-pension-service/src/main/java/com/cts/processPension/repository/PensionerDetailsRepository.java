package com.cts.processPension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.processPension.model.PensionerDetail;

/**
 * This Repository is for Pensioner Details. When the input we give here in
 * Process Pension Microservice gets matched with the details from Pensioner
 * Detail Microservice, then the pensioner details are to be saved into the
 * database
 * 
 * @author Abhishek Kumar
 *
 */

@Repository
public interface PensionerDetailsRepository extends JpaRepository<PensionerDetail, String> {

}
