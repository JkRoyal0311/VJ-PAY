package com.coforge.daos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coforge.entities.Beneficiary;
import com.coforge.repositories.BeneficiaryRepo;

@Repository
public class BeneficiaryDao implements BeneficiaryDaoInterface {

	@Autowired
	BeneficiaryRepo repository;



		@Override
		public List<Beneficiary> getAllBeneficiary() {
			// TODO Auto-generated method stub
			return repository.findAll();
		}

		@Override
		public Beneficiary saveBeneficiary(Beneficiary beneficiary) {
			// TODO Auto-generated method stub

			

			return repository.save(beneficiary);
		}

		@Override
		public Optional<Beneficiary> getBeneficiaryById(long id) {
			// TODO Auto-generated method stub
			return repository.findById(id);
		}

		

		@Override
		public void deleteBeneficiary(long id) {
			// TODO Auto-generated method stub
			repository.deleteById(id);
		}
		@Override
		public Optional<Beneficiary> findByBeneficiaryName(String beneficiaryName) {
			// TODO Auto-generated method stub
	        return repository.findByBeneficiaryName(beneficiaryName);

		}
		  @Override
		 public Optional<Beneficiary> findFirstByMobileNumber(String mobileNumber) {
		        return repository.findFirstByMobileNumber(mobileNumber);
		    }

	}


