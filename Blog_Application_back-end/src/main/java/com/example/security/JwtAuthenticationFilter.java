package com.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//a) get token from request --> eg.  Bearer 23467kjk78
		String requestToken = request.getHeader("Authorization");
		System.out.println(requestToken);
		String username= null;
		String token= null;
		if(requestToken !=null && requestToken.startsWith("Bearer")) 
		{//Extract token
		 token = requestToken.substring(7);
		 try {
		 username = this.jwtTokenHelper.getUsernameFromToken(token);
		 }catch(IllegalArgumentException e) {
			// e.getMessage();
			 System.out.println("Unable to get token");
		 }catch(ExpiredJwtException e) {
			 System.out.println("Token get expired");
		 }catch(MalformedJwtException e) {
			 System.out.println("Invalid Token");
		 }
		}else {
			System.out.println("Jwt Token does not begin with Bearer");
		}
		
		//b)validate token
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) 
			{//If it gives true means everything is good and then authenticate the token
				//create Obj of authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}else {
				System.out.println("Invalid token");
			}
		}else {
			System.out.println("username is null or context is not null");
		}
		
		filterChain.doFilter(request, response);
	}

}
