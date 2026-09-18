package com.coforge.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.entities.Beneficiary;
import com.coforge.services.BeneficiaryService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class BeneficiaryController {
	@Autowired
	private BeneficiaryService service;

	@GetMapping("/beneficiary")
	public ResponseEntity<List<Beneficiary>> getAllBeneficiary() {
		return new ResponseEntity<>(service.getAllBeneficiary(), HttpStatus.OK);
	}

	@PostMapping("/beneficiary")
	public ResponseEntity<Map<String, String>> saveBeneficiary(@RequestBody Beneficiary beneficiary) {

		// Student student = new Student();

		// student.setDob(service.parseDob(student.getDob())); // manual DOB parsing
		String response = service.addBeneficiary(beneficiary);

		return new ResponseEntity<>(Map.of("message", response), HttpStatus.CREATED);
	}

	@GetMapping("/beneficiary/{cid}")
	public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable("cid") long cid) {
		return new ResponseEntity<Beneficiary>(service.getBeneficiaryById(cid), HttpStatus.OK);
	}

	@DeleteMapping("/beneficiary/{bid}")
	public ResponseEntity<Map<String, String>> deleteBeneficiary(@PathVariable("bid") long bid) {

		service.deleteBeneficiary(bid);
		return new ResponseEntity<>(Map.of("message", "Beneficiary deleted successfully"), HttpStatus.OK);
	}

	@GetMapping("/mobile/{mobileNumber}")
	public ResponseEntity<Beneficiary> getBeneficiaryByMobile(@PathVariable String mobileNumber) {

		return service.findFirstByMobileNumber(mobileNumber)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("beneficiary/name/{name}")
	public ResponseEntity<Beneficiary> getBeneficiaryByName(@PathVariable String name) {

		return service.findByBeneficiaryName(name)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/beneficiary/mobile/sendMoney/{mobileNumber}")
	public ResponseEntity<Map<String, String>> transferMoneyToBeneficiaryByMobile(@RequestParam("amount") String amount,
			@PathVariable("mobileNumber") String mobileNumber) {
		String response = service.sendMoney(mobileNumber, Double.parseDouble(amount));
		return new ResponseEntity<>(Map.of("message", response), HttpStatus.OK);
	}
}
