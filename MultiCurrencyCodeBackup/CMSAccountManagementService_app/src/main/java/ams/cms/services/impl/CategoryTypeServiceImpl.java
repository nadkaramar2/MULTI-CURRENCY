package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.CategoryTypeDao;
import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;
import ams.cms.services.CategoryTypeService;

@Transactional
@Service
public class CategoryTypeServiceImpl implements CategoryTypeService  
{
	@Autowired
	CategoryTypeDao  categoryTypeDao;

	@Override
	public List<Categorytype> getAllListOfcategorytype(Categorytype categorytype)
	{
		return categoryTypeDao.getAllListOfcategorytype(categorytype);
	}

	 @Override 
	 public Boolean isCategoryTypeAlreadyExist(CategoryListModel categoryListModel) {
	  return categoryTypeDao.getAllListOfcategorytype(categoryListModel); 
	 }
	
}
