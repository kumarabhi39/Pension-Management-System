package com.cts.processPension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.processPension.model.BankCharges;

/**
 * This Repository is for Bank Charges
 * 
 * @author Abhishek Kumar
 *
 */
@Repository
public interface BankChargesRepository extends JpaRepository<BankCharges, Integer> {

	List<BankCharges> findByBankType(String bankType);

}
