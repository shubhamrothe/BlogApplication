package com.example.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.entities.Category;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.exceptions.ResourceNotFoundException;
import com.example.payloads.PostDto;
import com.example.payloads.PostResponse;
import com.example.repositories.CategoryRepository;
import com.example.repositories.PostRepository;
import com.example.repositories.UserRepository;
import com.example.services.PostServiceI;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostServiceI {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		log.info("Initiating the dao call to create a Post for user of userId: {} and for category of categoryId: {}", userId, categoryId);
		// to fetch user
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		// to fetch category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepository.save(post);
		log.info("Completed the dao call to create a Post for user of userId: {} and for category of categoryId: {}", userId, categoryId);
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePostById(PostDto postDto, Integer postId) {
		log.info("Initiating the dao call to update a Post of postId: {}",postId);
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		post.setPostTitle(postDto.getPostTitle());
		post.setPostContent(postDto.getPostContent());
		Post updatedPost = this.postRepository.save(post);
		log.info("Completed the dao call to update a Post of postId: {}",postId);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
		
		log.info("Initiating the dao call to get all Posts of pageNumber: {} and sortBy: {} in direction: {}", pageNumber,sortBy, sortDirection );
		Sort sort=(sortDirection.equalsIgnoreCase("asc"))?sort=Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
		
		Pageable p= PageRequest.of(pageNumber, pageSize, sort);
		Page<Post> pagePost = this.postRepository.findAll(p);
		List<Post> allPosts = pagePost.getContent();
		List<PostDto> listOfPostDto = allPosts.stream().map((post)->this.modelMapper.map(post, PostDto.class))
		.collect(Collectors.toList());
		//without pagination
//		List<Post> listOfPosts = this.postRepository.findAll();
//		List<PostDto> listOfPostDto = listOfPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());	
//		return listOfPostDto;
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(listOfPostDto);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		log.info("Completed the dao call to get all Posts of pageNumber: {} and sortBy: {} in direction: {}", pageNumber, sortBy, sortDirection);
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		log.info("Initiating the dao call to get a Post of postId: {}",postId);
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		log.info("Completed the dao call to get a Post of postId: {}",postId);
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public void deletePostById(Integer postId) {
		log.info("Initiating the dao call to delete a Post of postId: {}",postId);
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
		log.info("Completed the dao call to delete a Post of postId: {}",postId);
		this.postRepository.delete(post);
	}
	
	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		log.info("Initiating the dao call to get a Post by Category of categoryId: {}",categoryId);
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		List<Post> listOfPost = this.postRepository.findByCategory(category);
		List<PostDto> listOfPostDto = listOfPost.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		log.info("Completed the dao call to get a Post of categoryId: {}",categoryId);
		return listOfPostDto;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		log.info("Initiating the dao call to get a Post by userId of userId: {}",userId);
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		List<Post> listOfPost = this.postRepository.findByUser(user);
		List<PostDto> listOfPostDto = listOfPost.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		log.info("Completed the dao call to get a Post of userId: {}",userId);
		return listOfPostDto;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
	List<Post> postsByKeyword = this.postRepository.findBypostTitleContaining(keyword);
	List<PostDto> listOfPostDto = postsByKeyword.stream().map((post)-> this.modelMapper.map(postsByKeyword, PostDto.class))
	.collect(Collectors.toList());
	return listOfPostDto;
	}

}
