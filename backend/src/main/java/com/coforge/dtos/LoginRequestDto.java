package com.coforge.dtos;
import lombok.Data;

@Data
public class LoginRequestDto
{
	private String email;
	private String pwd;
}