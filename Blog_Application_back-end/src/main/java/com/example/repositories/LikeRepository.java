package com.example.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Like;
import com.example.entities.Post;
import com.example.entities.User;

public interface LikeRepository extends JpaRepository<Like, Integer> {
	Optional<Like> findByPostAndUser(Post post, User user);
	long countByPost(Post post);
}
