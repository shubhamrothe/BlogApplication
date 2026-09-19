package com.example.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

	private String token;
	private Integer userId;
	private Set<String> roles;
	
}
