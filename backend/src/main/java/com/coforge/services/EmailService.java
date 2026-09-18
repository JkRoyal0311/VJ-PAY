package com.coforge.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public String sendOtp(String toEmail) {
		String otp=String.valueOf(100000+new Random().nextInt(900000));
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("VJ Pay - Your Account Verification OTP");
		
		String emailBody = "Hello,\n\n"
				+ "Welcome to VJ Pay! To verify your account, please use the following One-Time Password (OTP):\n\n"
				+ "OTP: " + otp + "\n\n"
				+ "This OTP is valid for a short period of time. Please do not share this code with anyone.\n\n"
				+ "If you did not request this verification, please ignore this email.\n\n"
				+ "Best regards,\n"
				+ "The VJ Pay Team";
				
		message.setText(emailBody);
		
		mailSender.send(message);
		System.out.println("OTP Sent");
		return otp;
	}
}
