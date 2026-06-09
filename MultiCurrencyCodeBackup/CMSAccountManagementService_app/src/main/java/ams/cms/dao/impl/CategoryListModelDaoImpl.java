package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CategoryListModelDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CategoryListModel;

@Repository
public class CategoryListModelDaoImpl extends AbstractGenericDao<CategoryListModel> implements CategoryListModelDao
{

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryListModel> getCategoryListModelsList(CategoryListModel categoryListModel)
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (categoryListModel.getStrParticipantID()!=null && categoryListModel.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", categoryListModel.getStrParticipantID()));
			}
			
			List<CategoryListModel> listData = (List<CategoryListModel>) criteria.list();
			return listData;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
}
