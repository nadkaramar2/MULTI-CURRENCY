package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.CategoryListModel;
import ams.cms.services.CategoryListModelService;

@RestController
@RequestMapping("/account_type_category")
public class CategoryListModelController 
{
	@Autowired
	CategoryListModelService categoryListModelService;
	
	@RequestMapping(value = "/getAccountTypeCategory", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTypeCategoryBasedOnParameter(@RequestBody CategoryListModel categoryListModel)
	{
		try 
		{
			List<CategoryListModel> categoryListModelsList = categoryListModelService.getCategoryListModelsList(categoryListModel);
			return ResponseEntity.ok(categoryListModelsList);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
