package com.example.payloads;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int userId;
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
