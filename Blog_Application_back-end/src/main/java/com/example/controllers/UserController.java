package com.example.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payloads.ApiResponse;
import com.example.payloads.UserDto;
import com.example.services.UserServiceI;



@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
	
	@Autowired
	private UserServiceI userServiceI;

	//POST
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		
		UserDto createUserDto = this.userServiceI.createUser(userDto);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);   
	}
	
	//PUT
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId){
		UserDto updatedUserDto = this.userServiceI.updateUserById(userDto, userId);
		return ResponseEntity.ok(updatedUserDto);
		
	}
	
	//DELETE
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Integer userId){
		this.userServiceI.deleteUserById(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully !", true), HttpStatus.OK);
		
	}
	
	//GET-ALL
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		List<UserDto> list = this.userServiceI.getAllUsers();
		return ResponseEntity.ok(list);
	}
	
	//GET
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
		return ResponseEntity.ok( this.userServiceI.getUserById(userId));
	}
}
