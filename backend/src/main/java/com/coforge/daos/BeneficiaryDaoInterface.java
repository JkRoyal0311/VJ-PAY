package com.coforge.daos;

import java.util.List;
import java.util.Optional;

import com.coforge.entities.Beneficiary;

public interface BeneficiaryDaoInterface {
	public List<Beneficiary> getAllBeneficiary();
	public Beneficiary saveBeneficiary(Beneficiary beneficiary);
	public Optional<Beneficiary> getBeneficiaryById(long id);
	 public Optional<Beneficiary> findFirstByMobileNumber(String mobileNumber);
	 public Optional<Beneficiary> findByBeneficiaryName(String beneficiaryName);
	void deleteBeneficiary(long id);
}
