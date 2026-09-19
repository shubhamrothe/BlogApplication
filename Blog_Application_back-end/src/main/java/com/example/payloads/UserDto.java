package com.example.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

	private Integer userId;
	@NotEmpty
	@Size(min=4, message="usrName must have minimum 4 charactures !!")
	private String userName;
	@Email(message="Email address is invalid !!")
	@Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	private String email;
	@NotEmpty
	@Size(min=4, max=10, message="Password must be of minimum 4 charactures and maximum of 10 charactures")
	private String password;
	@NotEmpty
	private String about;
	//
}
