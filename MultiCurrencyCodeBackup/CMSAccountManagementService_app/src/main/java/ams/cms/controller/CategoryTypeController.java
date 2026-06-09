package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;
import ams.cms.services.CategoryListModelService;
import ams.cms.services.CategoryTypeService;

@RestController
@RequestMapping("/category_type")
public class CategoryTypeController {
	
	@Autowired
	CategoryTypeService categoryTypeService;
	
	@Autowired
	CategoryListModelService categoryListModelService;
	
	
	@RequestMapping(value = "/categoryTypeData", method = RequestMethod.POST)
	public ResponseEntity<?> Categorytypedata(@RequestBody Categorytype categorytype)
	{
		String result = null;
		try 
		{
			List<Categorytype> category = categoryTypeService.getAllListOfcategorytype(categorytype);
			if (category!=null && category.size() > 0) 
			{
				return ResponseEntity.ok(category);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}	


	@RequestMapping(value = "/categorytypeExit", method = RequestMethod.POST)
	public ResponseEntity<?> categorytypeExit(@RequestBody CategoryListModel categoryListModel) {
		Boolean categorytypeExit = false;
	
		try 
		{
			categorytypeExit = categoryTypeService.isCategoryTypeAlreadyExist(categoryListModel);
			return ResponseEntity.ok(categorytypeExit);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(categorytypeExit);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveCategoryType(@RequestBody CategoryListModel categoryListModel) 
	{
	
	try {
		categoryListModel = categoryListModelService.saveCategoryListModel(categoryListModel);
		if(categoryListModel!=null && categoryListModel.getId()>0) 
		{
			return ResponseEntity.ok(categoryListModel);
		}
	} 
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return ResponseEntity.ok(null);
	
	
	}
	
		
}
