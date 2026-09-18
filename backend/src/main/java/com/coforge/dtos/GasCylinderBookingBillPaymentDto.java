package com.coforge.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GasCylinderBookingBillPaymentDto {
	private String gasProvider;
	private String customerNumber;
}
