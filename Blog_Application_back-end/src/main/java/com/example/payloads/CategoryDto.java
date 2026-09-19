package com.example.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	@NotEmpty
	@Size(min=4, message="Category title must be of minimum 4 charactues !!")
	private String categoryTitle;
	@NotEmpty
	@Size(min=15, message="Category description must be of minimum 15 charactues !!")
	private String categoryDescription;
	private String createdBy;
	private String modifiedBy;
	private Date createdAt;
	private Date modifiedAt;
}
