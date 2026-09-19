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
import com.example.entities.User;
import com.example.security.JwtTokenHelper;
import com.example.payloads.*;
import com.example.services.UserServiceI;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserServiceI userServiceI;

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
		User user = (User) userDetails;
		JwtAuthResponse response = new JwtAuthResponse(token, user.getUserId(),
				user.getRoles().stream().map(role -> role.getRoleName()).collect(java.util.stream.Collectors.toSet()));
		// response.setToken(token); --> The JwtAuthResponse constructor already takes
		// care of setting the token, so no need to call setToken again
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<java.util.Map<String, String>> forgotPassword(
			@Valid @RequestBody ForgotPasswordRequest request) {
		String token = this.userServiceI.createPasswordResetToken(request);
		java.util.Map<String, String> response = new java.util.HashMap<>();
		response.put("message", "If the email exists, a reset token has been generated.");
		if (token != null) {
			response.put("resetToken", token);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<com.example.payloads.ApiResponse> resetPassword(
			@Valid @RequestBody ResetPasswordRequest request) {
		this.userServiceI.resetPassword(request);
		return ResponseEntity.ok(new com.example.payloads.ApiResponse("Password reset successfully", true));
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
