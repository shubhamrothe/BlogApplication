package com.example.services;

import com.example.payloads.CommentDto;


public interface CommentServiceI {

	CommentDto createComment(CommentDto commentDto, Integer postId);
	
	void deleteComment(Integer commentId);
}
