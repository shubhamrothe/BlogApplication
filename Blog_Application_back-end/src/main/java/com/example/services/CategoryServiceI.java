package com.example.services;

import java.util.List;

import com.example.payloads.CategoryDto;

public interface CategoryServiceI {

	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategoryById(CategoryDto categoryDto, Integer categoryId);
	
	void deleteCategoryById(Integer categoryId);
	
	CategoryDto getCategoryById(Integer categoryId);
	
	List<CategoryDto> getAllCategory();
}
