package com.example.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.configuration.AppConstants;
import com.example.payloads.ApiResponse;
import com.example.payloads.PostDto;
import com.example.payloads.PostResponse;
import com.example.services.FileServiceI;
import com.example.services.PostServiceI;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//@Validated
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostServiceI postServiceI;
	
	@Autowired
	private FileServiceI fileServiceI;
	
	@Value("${project.image}")
	private String path; 

	// create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		log.info("Requesting to create a Post for a user of userId: {}", userId);
		PostDto createPost = this.postServiceI.createPost(postDto, userId, categoryId);
		log.info("Completed the request to create a Post for a user of userId: {}", userId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}

	// update post by Id
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postDto, @PathVariable Integer postId) {
		log.info("Requesting to update a Post by postId: {}", postId);
		PostDto updatePostDto = this.postServiceI.updatePostById(postDto, postId);
		log.info("Completed the request to delete a Post by postId: {}", postId);
		return new ResponseEntity<PostDto>(updatePostDto, HttpStatus.OK);
	}

	// get All posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value="pageNumber",defaultValue=AppConstants.PAGE_NUMBER, required =false ) Integer pageNumber,
			@RequestParam(value="pageSize",defaultValue =AppConstants.PAGE_SIZE, required= false) Integer pageSize,
			@RequestParam(value="sortBy",defaultValue=AppConstants.SORT_BY,required=false) String sortBy,
			@RequestParam(value="sortDirection",defaultValue=AppConstants.SORT_DIRECTION,required=false) String sortDirection){
		log.info("Requesting to get all Posts of pageNumber: {} and sortBy: {}", pageNumber, sortBy );
		PostResponse postResponse = this.postServiceI.getAllPost(pageNumber, pageSize, sortBy, sortDirection);
		log.info("Completed the request to  get all Posts of pageNumber: {} and sortBy: {}", pageNumber, sortBy );
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	// get posts by Id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getAllPostById(@PathVariable Integer postId) {
		log.info("Requesting to get all Posts by postId: {}", postId);
		PostDto postById = this.postServiceI.getPostById(postId);
		log.info("Completed the request to get all Posts by postId: {}", postId);
		return new ResponseEntity<PostDto>(postById, HttpStatus.OK);
	}

	// delete post by Id
	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePostById(@PathVariable Integer postId) {
		log.info("Requesting to delete a Post by postId: {}", postId);
		this.postServiceI.deletePostById(postId);
		log.info("Completed the request to delete a Post by postId: {}", postId);
		return new ApiResponse("Post is successfully deleted!!", true);
	}

	// get by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {
		log.info("Requesting to get a Post by category of categoryId: {}", categoryId);
		List<PostDto> listOfPosts = this.postServiceI.getPostsByCategory(categoryId);
		log.info("Completed the request to get a Post by category of categoryId: {}", categoryId);
		return new ResponseEntity<List<PostDto>>(listOfPosts, HttpStatus.OK);
	}

	// get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
		log.info("Requesting to get a Post by user of userId: {}", userId);
		List<PostDto> listOfPosts = this.postServiceI.getPostsByUser(userId);
		log.info("Completed the request to get a Post by user of userId: {}", userId);
		return new ResponseEntity<List<PostDto>>(listOfPosts, HttpStatus.OK);
	}

	//search post by keyword
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords")  String keywords){
		List<PostDto> posts = this.postServiceI.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);	
	}
	
	//post image upload
//	@PostMapping("/post/image/upload/{postId}")
//	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
//			@PathVariable Integer postId) throws IOException{
//		
//		PostDto postDto = this.postServiceI.getPostById(postId);
//		
//		String fileName = this.fileServiceI.uploadImage(path, image);
//		postDto.setImageName(fileName);
//		PostDto updatePostById = this.postServiceI.updatePostById(postDto, postId);
//	return new ResponseEntity<PostDto>(updatePostById, HttpStatus.OK);
//	}
}
