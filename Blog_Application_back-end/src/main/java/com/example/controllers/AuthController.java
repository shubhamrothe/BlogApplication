package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payloads.JwtAuthRequest;
import com.example.payloads.JwtAuthResponse;
import com.example.security.JwtTokenHelper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	// Login API
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {

		// Authenticate username and password
		authenticate(request.getEmail(), request.getPassword());

		// Find userDetails
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());

		// Generate token
		String token = this.jwtTokenHelper.generateToken(userDetails);

		// Return response
		JwtAuthResponse response = new JwtAuthResponse(token);
		// response.setToken(token); --> The JwtAuthResponse constructor already takes
		// care of setting the token, so no need to call setToken again
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void authenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			// You can log this if necessary
			throw new BadCredentialsException("Invalid username or password", e);
		}

	}
}
