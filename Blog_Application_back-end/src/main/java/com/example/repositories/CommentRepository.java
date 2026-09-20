package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Comment;
import com.example.entities.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	long countByPost(Post post);
}
