
package com.example.services;

import java.util.List;

import com.example.payloads.UserDto;
import com.example.entities.User;
import com.example.payloads.ChangePasswordRequest;

public interface UserServiceI {

	UserDto createUser(UserDto user); 
	
	UserDto getUserById(Integer userId);
	
	UserDto updateUserById(UserDto user, Integer userId);
	UserDto updateUserById(UserDto user, Integer userId, User actor);

	void changePassword(ChangePasswordRequest request, User actor);
	
	List<UserDto> getAllUsers();
	
	void deleteUserById(Integer userId);
	
	
}