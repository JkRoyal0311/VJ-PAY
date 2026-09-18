package com.coforge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerJWTTokenDto {
	private long custId;
	private String custName;
	private String mobileNumber;
	private String email;
	private String role;
}
