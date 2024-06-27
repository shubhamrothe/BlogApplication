package com.example.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Category;
import com.example.exceptions.ResourceNotFoundException;
import com.example.payloads.CategoryDto;
import com.example.repositories.CategoryRepository;
import com.example.services.CategoryServiceI;

@Service
public class CategoryServiceImpl implements CategoryServiceI {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	//CREATE
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category saved = this.categoryRepository.save(category);
		return this.modelMapper.map(saved, CategoryDto.class);
	}

	//UPDATE
	@Override
	public CategoryDto updateCategoryById(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		Category updatedCategory = this.categoryRepository.save(category);
		return this.modelMapper.map(updatedCategory, CategoryDto.class);
	}

	//DELETE
	@Override
	public void deleteCategoryById(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
		this.categoryRepository.delete(category);
	}

	//GETById
	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));
		return this.modelMapper.map(category, CategoryDto.class);
	}

	//GETALL
	//@SuppressWarnings("unchecked")
	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> catagories = this.categoryRepository.findAll();
		//return (List<CategoryDto>) this.modelMapper.map(findAll, CategoryDto.class);
		List<CategoryDto> listOfCatDto = catagories.stream().map((cat)-> this.modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		return listOfCatDto;
	}

}
