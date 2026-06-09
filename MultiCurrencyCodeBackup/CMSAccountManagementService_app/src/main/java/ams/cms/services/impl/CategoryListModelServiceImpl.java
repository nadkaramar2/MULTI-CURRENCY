package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.CategoryListModelDao;
import ams.cms.model.CategoryListModel;
import ams.cms.services.CategoryListModelService;

@Transactional
@Service
public class CategoryListModelServiceImpl implements CategoryListModelService 
{
	@Autowired
	CategoryListModelDao categoryListModelDao;
	
	@Override
	public List<CategoryListModel> getCategoryListModelsList(CategoryListModel categoryListModel)
	{
		return categoryListModelDao.getCategoryListModelsList(categoryListModel);
	}

	@Override
	public CategoryListModel saveCategoryListModel(CategoryListModel categoryListModel) throws Exception {
		categoryListModelDao.save(categoryListModel);		
		return categoryListModel;
	}

}
