package ams.cms.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

//added by ankit
import ams.cms.api.dao.ComplaintTypeDao;
import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.ComplaintType;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class ComplaintTypeDaoImpl extends AbstractGenericDao<ComplaintType> implements ComplaintTypeDao
{
	@Autowired
	JdbcTemplate jdbcCMSTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> findAllByComplaintType(AccountRequest accountRequest)
	{
		String sql = "SELECT complaint_type as strComplaintType FROM complaint_type WHERE is_credit_type = (SELECT is_credit_type FROM account_type_master WHERE account_type = ?)";
		try {
		
			List<String> resultStrings = new ArrayList<>();
			List<ComplaintType> complaintTypes = this.jdbcCMSTemplate.query(sql,
				 (RowMapper) new BeanPropertyRowMapper(ComplaintType.class),
				 new Object[] 
				{
							accountRequest.getStrAccountType()
				}
				);
			for (int i = 0; i < complaintTypes.size(); i++) {
				resultStrings.add(complaintTypes.get(i).getStrComplaintType());
			}
			return resultStrings;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
