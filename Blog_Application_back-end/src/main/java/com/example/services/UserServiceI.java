
package com.example.services;

import java.util.List;

import com.example.payloads.UserDto;
import com.example.entities.User;

public interface UserServiceI {

	UserDto createUser(UserDto user); 
	
	UserDto getUserById(Integer userId);
	
	UserDto updateUserById(UserDto user, Integer userId);
	UserDto updateUserById(UserDto user, Integer userId, User actor);
	
	List<UserDto> getAllUsers();
	
	void deleteUserById(Integer userId);
	
	
}