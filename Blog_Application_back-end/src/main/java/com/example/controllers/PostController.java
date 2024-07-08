package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payloads.ApiResponse;
import com.example.payloads.PostDto;
import com.example.services.PostServiceI;

@RestController
@RequestMapping("/api")  
public class PostController {
	
	@Autowired
	private PostServiceI postServiceI;
	
	//create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer categoryId ){
		PostDto createPost = this.postServiceI.createPost(postDto, userId, categoryId);
		
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}
	
	//get by category
		@GetMapping("/category/{categoryId}/posts")
		public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId){
			List<PostDto> listOfPosts = this.postServiceI.getPostsByCategory(categoryId);
			return new ResponseEntity<List<PostDto>>(listOfPosts, HttpStatus.OK);
		}
		
	//get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
		List<PostDto> listOfPosts = this.postServiceI.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(listOfPosts, HttpStatus.OK);
	}
	
	//get All posts
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPost(){
		List<PostDto> allPost = this.postServiceI.getAllPost();
		return new ResponseEntity<List<PostDto>>(allPost, HttpStatus.OK);
		
	}
	
	//get posts by Id
		@GetMapping("/posts/{postId}")
		public ResponseEntity<PostDto> getAllPostById(@PathVariable Integer postId){
			PostDto postById = this.postServiceI.getPostById(postId);
			return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
			
		}
		
		//delete post by Id
		@DeleteMapping("/posts/{postId}")
		public ApiResponse deletePostById(@PathVariable Integer postId) {
			this.postServiceI.deletePostById(postId);
			return new ApiResponse("Post is successfully deleted!!", true);
			
		}
		
		//update post by Id
				@PutMapping("/posts/{postId}")
				public ResponseEntity<PostDto> updatePostById(@RequestBody PostDto postDto,@PathVariable Integer postId) {
					PostDto updatePostDto = this.postServiceI.updatePostById(postDto, postId);
					return new ResponseEntity<PostDto>(updatePostDto, HttpStatus.OK);
					
				}
}
