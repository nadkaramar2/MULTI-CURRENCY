package ams.cms.dao;

import java.util.List;

import ams.cms.model.CategoryListModel;

public interface CategoryListModelDao extends GenericDao<CategoryListModel>
{
	List<CategoryListModel> getCategoryListModelsList(CategoryListModel categoryListModel);
}
