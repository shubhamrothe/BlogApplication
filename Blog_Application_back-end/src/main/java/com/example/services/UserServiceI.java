
package com.example.services;

import java.util.List;

import com.example.payloads.UserDto;

public interface UserServiceI {

	UserDto createUser(UserDto user); 
	
	UserDto getUserById(Integer userId);
	
	UserDto updateUserById(UserDto user, Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUserById(Integer userId);
	
	
}