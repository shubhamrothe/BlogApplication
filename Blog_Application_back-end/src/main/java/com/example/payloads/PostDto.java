package com.example.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

	private Integer postId;
	@NotEmpty
	@Size(min=4, max=100, message="postTitle must be of minimum 4 charactures and maximum of 100 charactures")
	private String postTitle;
	@NotEmpty
	@Size(min=50, max=1000, message="postContent must be of minimum 50 charactures and maximum of 1000 charactures")
	private String postContent;
	private String imageName;
	//@NotEmpty
	private Date addedDate;
	//@NotEmpty
	private CategoryDto category;
	//@NotEmpty
	private UserDto user;
	
	private Set<CommentDto> comments = new HashSet<>();
}
