package com.example.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

	private Integer commentId;
	@NotEmpty
	@Size(min=2, max=50, message="commentContent must be of minimum 2 charactures and maximum of 50 charactures")
	private String commentContent;
}
