package com.example.services;

import java.util.List;

import com.example.payloads.CategoryDto;
import com.example.entities.User;

public interface CategoryServiceI {

	CategoryDto createCategory(CategoryDto categoryDto, User actor);
	
	CategoryDto updateCategoryById(CategoryDto categoryDto, Integer categoryId, User actor);
	
	void deleteCategoryById(Integer categoryId);
	
	CategoryDto getCategoryById(Integer categoryId);
	
	List<CategoryDto> getAllCategory();
}
