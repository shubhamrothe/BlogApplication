package com.example.services;

import java.util.List;

import com.example.payloads.PostDto;
import com.example.payloads.PostResponse;

public interface PostServiceI {

	//CREATE
	PostDto createPost (PostDto postDto,Integer userId, Integer categoryId);
	
	//UPDATE
	PostDto updatePostById(PostDto postDto, Integer postId);
	
	//GET ALL
	//List<PostDto>
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
	
	//GET
	PostDto getPostById(Integer postId);
	
	//DELETE
	void deletePostById(Integer postId);
	
	//GET ALL POST BY CATEGORY
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	//GET ALL POST BY USER
	List<PostDto> getPostsByUser(Integer userId); 
	
	//SEARCH POSTS BY KEYWORD
	List<PostDto> searchPosts(String keyword);  
}
