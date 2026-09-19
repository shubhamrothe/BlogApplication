package com.example.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name="post")
public class Post {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer postId;
	@Column(name="post_title")
	private String postTitle;
	@Column(name="post_content")
	private String postContent;
	@Column(name="img_name")
	private String imageName;
	@Column(name="date_of_post_creation")
	private Date addedDate;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "modified_by")
	private String modifiedBy;
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "modified_at")
	private Date modifiedAt;
	@Column(name = "like_count")
	private Integer likeCount = 0;
	@Column(name = "comment_count")
	private Integer commentCount = 0;
	@Column(name = "share_count")
	private Integer shareCount = 0;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	 @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	 private Set<Comment> comments = new HashSet<>();

	 @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	 private Set<Like> likes = new HashSet<>();
}
