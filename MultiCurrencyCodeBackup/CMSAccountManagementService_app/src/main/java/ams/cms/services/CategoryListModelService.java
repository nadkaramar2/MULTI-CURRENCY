package ams.cms.services;

import java.util.List;

import ams.cms.model.CategoryListModel;

public interface CategoryListModelService 
{
	List<CategoryListModel> getCategoryListModelsList(CategoryListModel categoryListModel) throws Exception;
	
	CategoryListModel saveCategoryListModel(CategoryListModel categoryListModel) throws Exception;
}
