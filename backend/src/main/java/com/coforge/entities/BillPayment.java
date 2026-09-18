package com.coforge.entities;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class BillPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long billId;
	
	@CreatedDate
	private LocalDateTime paymentDate;
	@Min(value=1,message="Min amount for bill payment is 1")
	private double amount;
	
	@NotNull(message = "Bill Type must not be null")
	private BillType billType;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json")
	@NotEmpty
	private Map<String, Object> billData;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="wallet_id")
	private Wallet wallet;
	
//	@OneToOne
//	private Transaction transaction;

	public BillPayment(double amount, BillType billType, Map<String, Object> billData) {
		super();
		this.amount = amount;
		this.billType = billType;
		this.billData = billData;
	}

	
}