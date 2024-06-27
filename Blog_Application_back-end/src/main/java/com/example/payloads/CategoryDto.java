package com.example.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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
}
