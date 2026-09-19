
package com.example.services;

import java.util.List;

import com.example.payloads.UserDto;
import com.example.entities.User;
import com.example.payloads.ChangePasswordRequest;
import com.example.payloads.ForgotPasswordRequest;
import com.example.payloads.ResetPasswordRequest;

public interface UserServiceI {

	UserDto createUser(UserDto user); 
	
	UserDto getUserById(Integer userId);
	
	UserDto updateUserById(UserDto user, Integer userId);
	UserDto updateUserById(UserDto user, Integer userId, User actor);

	void changePassword(ChangePasswordRequest request, User actor);
	String createPasswordResetToken(ForgotPasswordRequest request);
	void resetPassword(ResetPasswordRequest request);
	
	List<UserDto> getAllUsers();
	
	void deleteUserById(Integer userId);
	
	
}