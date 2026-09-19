package com.example.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

	@NotEmpty
	private String currentPassword;

	@NotEmpty
	@Size(min = 4, max = 10, message = "Password must be of minimum 4 characters and maximum of 10 characters")
	private String newPassword;
}
