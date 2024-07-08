package com.example.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Category;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.exceptions.ResourceNotFoundException;
import com.example.payloads.PostDto;
import com.example.repositories.CategoryRepository;
import com.example.repositories.PostRepository;
import com.example.repositories.UserRepository;
import com.example.services.PostServiceI;

@Service
public class PostServiceImpl implements PostServiceI{

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public PostDto createPost(PostDto postDto,Integer userId, Integer categoryId) {
		//to fetch user
		User user = this.userRepository.findById(userId)
			.orElseThrow(()-> new ResourceNotFoundException("User","userId",userId));
		//to fetch category
		Category category = this.categoryRepository.findById(categoryId)
		.orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepository.save(post);
		return this.modelMapper.map(newPost, PostDto.class);
	}

	
	
	@Override
	public PostDto updatePostById(PostDto postDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
					.orElseThrow(()-> new ResourceNotFoundException("Post","postId",postId));
		 post.setPostTitle(postDto.getPostTitle());
		 post.setPostContent(postDto.getPostContent());
		 Post updatedPost = this.postRepository.save(post);
		
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePostById(Integer postId) {
	 Post post = this.postRepository.findById(postId)
	 					.orElseThrow(()-> new ResourceNotFoundException("Post","postId",postId));
		
	 this.postRepository.delete(post);
		//return null;
	}

	@Override
	public List<PostDto> getAllPost() {
		List<Post> listOfPosts = this.postRepository.findAll();
		List<PostDto> listOfPostDto = listOfPosts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return listOfPostDto;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId) .orElseThrow(()->new ResourceNotFoundException("Post","postId", postId));
		
		return this.modelMapper.map(post, PostDto.class);	
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
												   .orElseThrow(()->new ResourceNotFoundException("Category","categoryId", categoryId));
		
		List<Post> listOfPost = this.postRepository.findByCategory(category);
		List<PostDto> listOfPostDto = listOfPost.stream().map((post)->this.modelMapper.map(post, PostDto.class))
														 .collect(Collectors.toList());
		return listOfPostDto;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepository.findById(userId)
									   .orElseThrow(()-> new ResourceNotFoundException("User","userId",userId));
		List<Post> listOfPost = this.postRepository.findByUser(user);
		List<PostDto> listOfPostDto = listOfPost.stream().map((post)->this.modelMapper.map(post, PostDto.class))
														 .collect(Collectors.toList());
		return listOfPostDto;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}
