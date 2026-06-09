package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ams.cms.dao.CategoryTypeDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CategoryListModel;
import ams.cms.model.Categorytype;

@Repository
public class CategoryTypeDaoImpl extends AbstractGenericDao<Categorytype> implements CategoryTypeDao {

	@Autowired
	JdbcTemplate jdbcCMSTemplate;

	@Autowired
	@Qualifier("jdbcTemplate")
	JdbcTemplate jdbcTemplate;



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Categorytype> getAllListOfcategorytype(Categorytype categorytype) 
	{
		try {
			List<Categorytype> categorytypes = this.jdbcCMSTemplate.query("SELECT description AS strDescription, type AS strType FROM category_type  ",
			(RowMapper) new BeanPropertyRowMapper(Categorytype.class),new Object[] {});
			System.out.println("ConfigurationDaoImpl.getCountry()" + categorytypes);
			
			return categorytypes;
		} 
		catch (Exception e)
		{
		e.printStackTrace();	
		}
	return null;
	}
	
	
	
	@Override
	public boolean getAllListOfcategorytype(CategoryListModel categoryListModel)
	{
		try
		{
			String sql = "SELECT count(*) FROM account_type_category_master aclc WHERE aclc.`type`=?";
			System.out.println(sql);
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] {categoryListModel.getStrType()});
			return count > 0;
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
}
}	
	
	



