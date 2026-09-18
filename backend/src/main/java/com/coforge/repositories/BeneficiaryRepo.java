package com.coforge.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.entities.Beneficiary;
//import com.coforge.entity.User;

public interface BeneficiaryRepo extends JpaRepository<Beneficiary, Long>{
public	Optional<Beneficiary> findFirstByMobileNumber(String mobileNumber);
public Optional<Beneficiary> findByBeneficiaryName(String beneficiaryName);

}
