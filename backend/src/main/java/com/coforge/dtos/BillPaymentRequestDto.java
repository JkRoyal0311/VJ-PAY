package com.coforge.dtos;

import java.time.LocalDate;
import java.util.Map;

import com.coforge.entities.BillType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillPaymentRequestDto {
	@NotEmpty
	private double amount;
	@NotNull(message = "Bill Type must not be null")
	private BillType billType;
	@NotEmpty
	private Map<String, Object> billData;
}
