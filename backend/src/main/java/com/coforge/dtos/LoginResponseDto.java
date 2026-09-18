package com.coforge.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class LoginResponseDto
{
	private String token;
	private String email;
}