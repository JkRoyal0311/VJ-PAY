package com.coforge.services;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coforge.entities.Beneficiary;
import com.coforge.entities.Customer;
import com.coforge.entities.Wallet;
import com.coforge.exception.BeneficiaryException;
import com.coforge.repositories.BeneficiaryRepo;

@Service
public class BeneficiaryAdminService {

    @Autowired
    private BeneficiaryRepo beneficiaryRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WalletServiceImpl walletService;

  
    public List<Beneficiary> viewAllBeneficiaries() {
        return beneficiaryRepo.findAll();
    }

   
    public Beneficiary viewBeneficiaryById(long beneficiaryId) {
        return beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryException("No beneficiary found with ID " + beneficiaryId));
    }

   
    public List<Beneficiary> viewBeneficiariesOfCustomer(long customerId) {

        Customer customer = customerService.getById(customerId);

        Wallet wallet = walletService.getWalletByCustomerId(customer.getCustId());

        return walletService.getWalletBeneficiaries(wallet.getWalletId());
    }

    // ✅ 4. Delete beneficiary (admin)
    public String deleteBeneficiary(long beneficiaryId) {

        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryException("No beneficiary found with ID " + beneficiaryId));

        beneficiaryRepo.delete(beneficiary);

        return "Beneficiary deleted successfully!";
    }

}