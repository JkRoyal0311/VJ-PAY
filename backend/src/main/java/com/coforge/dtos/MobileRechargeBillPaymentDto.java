package com.coforge.dtos;

import com.coforge.exception.InvalidBillPaymentDataException;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class MobileRechargeBillPaymentDto {
	@Pattern(regexp = "[6-9][0-9]{9}", message = "Phone number must be exactly 10 digits")
	private String mobileNumber;
	private MobileRechargeBillPaymentOperator operator;
	
	public MobileRechargeBillPaymentDto(
			@Pattern(regexp = "[6-9][0-9]{9}", message = "Phone number must be exactly 10 digits") String mobileNumber,
			MobileRechargeBillPaymentOperator operator) {
		super();
		
		if(mobileNumber.length()<10) {
			throw new InvalidBillPaymentDataException("Mobile Number Should be of length 10");
		}
		this.mobileNumber = mobileNumber;
		this.operator = operator;
	}
	
	
}
