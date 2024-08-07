package com.example.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.User;
import com.example.exceptions.ResourceNotFoundException;
import com.example.payloads.UserDto;
import com.example.repositories.UserRepository;
import com.example.services.UserServiceI;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserServiceI {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	// TO CREATE
	@Override
	public UserDto createUser(UserDto userDto) {
		log.info("Initiating the dao call to create a User");
		User toUser = this.dtoToUser(userDto);
		User savedUser = this.userRepository.save(toUser);
		UserDto toUserDto = this.userToDto(savedUser);
		log.info("Completed the dao call to create a User");
		return toUserDto;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		log.info("Initiating the dao call to get a user of userId: {}", userId);
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		log.info("Completed the service dao call to get a User of userId: {}", userId);
		return this.userToDto(user);
	}

	// TO UPDATE BY ID

	@Override
	public UserDto updateUserById(UserDto userDto, Integer userId) {
		log.info("Initiating the dao call to update the user of userId: {}", userId);
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));

		// User user = optionalUser.get(); // extract the user from the Optional

		// update the user properties from the userDto
		user.setUserName(userDto.getUserName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());

		// save the updated user
		User updatedUser = this.userRepository.save(user);

		// convert the updated user to a UserDto
		UserDto toUserDto = this.userToDto(updatedUser);
		log.info("Completed the dao call to update the user of userId: {}", userId);
		return toUserDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Initiating the dao call to  user of get all Users");
		List<User> users = this.userRepository.findAll();
		List<UserDto> list = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		log.info("Completed the dao call to get all Users");
		return list;
	}

	@Override
	public void deleteUserById(Integer userId) {
		log.info("Initiating the dao call to delete a user of userId: {}", userId);
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		log.info("Completed the dao call to delete a User of userId: {}", userId);
		this.userRepository.delete(user);
	}

	private User dtoToUser(UserDto userDto) {
//		User user= new User();
//		user.setUserId(userDto.getUserId());
//		user.setUserName(userDto.getUserName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
//		return user;	
		// OR
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	private UserDto userToDto(User user) {
//		UserDto userDto = new UserDto();
//		userDto.setUserId(user.getUserId());
//		userDto.setUserName(user.getUserName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
//		return userDto;	
		// OR
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
