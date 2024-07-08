package com.example.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category_details")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(name = "category_title")
	private String categoryTitle;

	@Column(name = "category_description")
	private String categoryDescription;
	
	//We can change these as per our requirement
	@OneToMany(mappedBy ="category", cascade=CascadeType.ALL, fetch= FetchType.LAZY)
	private List<Post> posts = new ArrayList<>();
}
