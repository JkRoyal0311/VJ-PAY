package com.coforge.dtos;
//import com.coforge.entities.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto
{
	private long custId;
	private String custName;
	private String mobileNumber;
	private String email;
	private WalletDto wallet;
}