package com.example.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Like;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.exceptions.ResourceNotFoundException;
import com.example.repositories.LikeRepository;
import com.example.repositories.PostRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class LikeController {
	private final LikeRepository likeRepository;
	private final PostRepository postRepository;

	@PostMapping("/{postId}/like")
	public ResponseEntity<Map<String, Object>> like(@PathVariable Integer postId, @AuthenticationPrincipal User user) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		likeRepository.findByPostAndUser(post, user).orElseGet(() -> {
			Like like = new Like();
			like.setPost(post);
			like.setUser(user);
			return likeRepository.save(like);
		});
		return ResponseEntity.ok(Map.of("liked", true, "likeCount", likeRepository.countByPost(post)));
	}

	@DeleteMapping("/{postId}/like")
	public ResponseEntity<Map<String, Object>> unlike(@PathVariable Integer postId, @AuthenticationPrincipal User user) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		likeRepository.findByPostAndUser(post, user).ifPresent(likeRepository::delete);
		return ResponseEntity.ok(Map.of("liked", false, "likeCount", likeRepository.countByPost(post)));
	}
}
