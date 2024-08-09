package com.example.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Comment;
import com.example.entities.Post;
import com.example.exceptions.ResourceNotFoundException;
import com.example.payloads.CommentDto;
import com.example.repositories.CommentRepository;
import com.example.repositories.PostRepository;
import com.example.services.CommentServiceI;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceImpl implements CommentServiceI{

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		log.info("Initiating the dao call to create a Comment for a post of postId: {}", postId);
		Post post = this.postRepository.findById(postId)
		.orElseThrow(()-> new ResourceNotFoundException("Post", "postId", postId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		Comment save = this.commentRepository.save(comment);
		log.info("Completed the dao call to create a Comment for a post of postId: {}", postId);
		return this.modelMapper.map(save, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		log.info("Initiating the dao call to delete a comment of commentId: {}",commentId);
		Comment comment = this.commentRepository.findById(commentId)
		.orElseThrow(()-> new ResourceNotFoundException("Comment","CommentId", commentId));
	    this.commentRepository.delete(comment);
	    log.info("Completed the dao call to delete a Comment of commentId: {}",commentId);
	}

}
