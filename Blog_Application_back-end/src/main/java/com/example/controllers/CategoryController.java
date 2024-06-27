package com.example.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payloads.ApiResponse;
import com.example.payloads.CategoryDto;
import com.example.services.CategoryServiceI;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryServiceI categoryServiceI;

	// CREATE
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto category = this.categoryServiceI.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(category, HttpStatus.CREATED);
	}

	// UPDATE
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategoryById(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Integer categoryId) {
		CategoryDto updateCategoryById = this.categoryServiceI.updateCategoryById(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updateCategoryById, HttpStatus.OK);
	}

	// DELETEById
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Integer categoryId) {
		this.categoryServiceI.deleteCategoryById(categoryId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Categoty deleted successfully !!", true),
				HttpStatus.OK);
	}

	// GETById
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer categoryId) {
		CategoryDto categoryById = this.categoryServiceI.getCategoryById(categoryId);
		return new ResponseEntity<CategoryDto>(categoryById, HttpStatus.OK);
	}

	// GETALL
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategory() {
		List<CategoryDto> allCategory = this.categoryServiceI.getAllCategory();
		return new ResponseEntity<List<CategoryDto>>(allCategory, HttpStatus.OK);
	}

}
