package ams.cms.dao;

import java.util.List;

import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;

public interface CategoryTypeDao extends GenericDao<Categorytype>
{
 List<Categorytype> getAllListOfcategorytype(Categorytype categorytype);

boolean getAllListOfcategorytype(CategoryListModel categoryListModel);
}
	
