package com.coforge.security;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.coforge.dtos.CustomerJWTTokenDto;
import com.coforge.dtos.EmailOtpDto;
import com.coforge.exception.InvalidOtpException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil
{
	private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 60 * 24;
	private static final long OTP_EXPIRATION_TIME = 1000 * 60 * 60 * 5;
	private final Key key;
	

	
	public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
	public String generateToken(CustomerJWTTokenDto customer ) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("custId",customer.getCustId());
		claims.put("custName",customer.getCustName());
		claims.put("mobileNumber",customer.getMobileNumber());
		claims.put("email",customer.getEmail());
		claims.put("role",customer.getRole());
        return Jwts.builder()
            .setSubject(customer.getEmail())
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
	
	public String generateOtpToken(String email,String otp) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email",email);
		claims.put("otp",otp);
        return Jwts.builder()
            .setSubject(otp)
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + OTP_EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
		
	public EmailOtpDto verifyOtpToken(String otpToken) {
		if(!this.validateToken(otpToken)) {
			throw new InvalidOtpException("Invalid Otp");
		}
		Claims claims =  Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(otpToken)
    			.getBody();
		
    	return new EmailOtpDto(claims.get("email", String.class),claims.get("otp", String.class));
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
    
    public CustomerJWTTokenDto extractCustomer(String token) {
    	Claims claims =  Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    	

    	return new CustomerJWTTokenDto(
        claims.get("custId", Long.class),
        claims.get("custName", String.class),
        claims.get("mobileNumber", String.class),
        claims.get("email", String.class),
    	claims.get("role", String.class));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("JWT error: " + e.getMessage());
            return false;
        }
    }
}