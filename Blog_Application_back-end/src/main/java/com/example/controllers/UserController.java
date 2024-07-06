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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {

	@Autowired
	private UserServiceI userServiceI;

	// POST
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		log.info("Requesting to create a User");
		UserDto createUserDto = this.userServiceI.createUser(userDto);
		log.info("Completed the request to create a User");
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	// PUT
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Integer userId) {
		log.info("Requesting to update a User of userId {}" + userId);
		UserDto updatedUserDto = this.userServiceI.updateUserById(userDto, userId);
		log.info("Completed the request to update a User of userId {}" + userId);
		return ResponseEntity.ok(updatedUserDto);
	}

	// DELETE
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Integer userId) {
		log.info("Requesting to delete a User of userId {}" + userId);
		this.userServiceI.deleteUserById(userId);
		log.info("Completed the request to delete a User of userId {}" + userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully !", true), HttpStatus.OK);
	}

	// GET-ALL
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		log.info("Requesting to retrive all Users");
		List<UserDto> list = this.userServiceI.getAllUsers();
		log.info("Completed the request to get all Users");
		return ResponseEntity.ok(list);
	}

	// GET
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId) {
		log.info("Requesting to retrive a User of userId {}" + userId);
		log.info("Completed the request to get a User of userId {}" + userId);
		return ResponseEntity.ok(this.userServiceI.getUserById(userId));
	}
}
