package ams.cms.services;

import java.util.List;

import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;

public interface CategoryTypeService {

	List<Categorytype> getAllListOfcategorytype(Categorytype categorytype);

	Boolean isCategoryTypeAlreadyExist(CategoryListModel categoryListModel);
}
