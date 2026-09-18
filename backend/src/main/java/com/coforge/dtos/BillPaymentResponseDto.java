package com.coforge.dtos;

import java.time.LocalDateTime;
import java.util.Map;

import com.coforge.entities.BillType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillPaymentResponseDto {
	private long billId;
	private LocalDateTime paymentDate;
	private double amount;
	private BillType billType;
	private Map<String, Object> billData;
}
