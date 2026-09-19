package com.example.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String token;
	@ManyToOne(optional = false)
	private User user;
	@Column(nullable = false)
	private Date expiresAt;
	@Column(nullable = false)
	private boolean used;
}
