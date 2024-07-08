package com.example.payloads;

import java.util.Date;

import com.example.entities.Category;
import com.example.entities.User;

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
	private String postTitle;
	private String postContent;
	private String imageName;
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
}
