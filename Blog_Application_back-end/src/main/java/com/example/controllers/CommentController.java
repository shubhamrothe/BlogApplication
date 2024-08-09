package com.example.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payloads.ApiResponse;
import com.example.payloads.CommentDto;
import com.example.services.CommentServiceI;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class CommentController {

	@Autowired
	private CommentServiceI commentServiceI;
	//To create Comment
	
	@PostMapping("/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Integer postId){
		log.info("Requesting to create a Comment for a post of postId: {}", postId);
		CommentDto createComment = this.commentServiceI.createComment(commentDto, postId);
		log.info("Completed the request to create a Comment for a post of postId: {}", postId);
		return new ResponseEntity<CommentDto>(createComment, HttpStatus.OK);
	}
	
	//To delete Comment
	@DeleteMapping("/{commentId}/comments")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
		log.info("Requesting to delete a Comment of commentId: {}", commentId);
		this.commentServiceI.deleteComment(commentId);
		log.info("Completed the request to delete a Comment of commentId: {}", commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully..!!",true), HttpStatus.OK);
		
	}
}
