package com.coforge.security;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter; 
import com.coforge.dtos.CustomerJWTTokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 
    @Autowired
    private JwtUtil jwtUtil;
    
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    	String path = request.getServletPath();
    	return !path.startsWith("/auth") && !path.startsWith("/admin");
	}

	@Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
 
        if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = header.substring(7);
 
            if (jwtUtil.validateToken(token)) {
//                String email = jwtUtil.extractEmail(token);
                CustomerJWTTokenDto customer = jwtUtil.extractCustomer(token);
                
                if(request.getServletPath().startsWith("/admin") && !customer.getRole().equals("ADMIN")) {
                	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
         
                    response.getWriter().write("""
                        {
                          "status": 401,
                          "error": "Unauthorized",
                          "message": "You are not allowed to access this route"
                        }
                    """);
         
                    return;
                }
                
                logger.info("Authorised User: {} {} {}",customer.getCustName(),customer.getMobileNumber(),customer.getEmail());
                
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
//                        email, null, Collections.emptyList()
                    		customer, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                auth.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                chain.doFilter(request, response);
            }
            else {
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
 
                response.getWriter().write("""
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Invalid or expired JWT token"
                    }
                """);
 
                return;
            }
        }
        else {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
 
            response.getWriter().write("""
                {
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "JWT token is required for this route"
                }
            """);
 
            return;
        }
    }
}