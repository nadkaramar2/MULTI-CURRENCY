package ams.cms.api.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.PreSubAccountMasterDao;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class PreSubAccountMasterDaoImpl extends AbstractGenericDao<PreSubAccountMaster> implements PreSubAccountMasterDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public String getAccountTypeExist(PreSubAccountMaster preSubAccMaster) {
		try
		{
			String sql = "SELECT account_type FROM pre_sub_account_master WHERE mobile_no  = '"+preSubAccMaster.getStrMobileNo()+"' AND account_type = '"+preSubAccMaster.getStrAccountType()+"'";
			/*
			String accTypeExist = jdbcTemplate.queryForObject(sql, String.class, 
					new Object[] 
					{ 
						preSubAccMaster.getStrMobileNo(), 
						preSubAccMaster.getStrAccountType() 
					});
			return accTypeExist;
			*/
			
			List<String> strLst = jdbcTemplate.query(sql, new RowMapper<String>() 
			{
			    public String mapRow(ResultSet rs, int rowNum) throws SQLException 
			    {
			        return rs.getString(1);
			    }
			});

			if (strLst.isEmpty()) 
			{
			    return null;
			}
			else if (strLst.size() == 1) 
			{ 
			    return strLst.get(0);
			} 
			else 
			{ // list contains more than 1 element
			  // either return 1st element or throw an exception
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateIsAccountNoCreatedField(PreSubAccountMaster preSubAccountMaster)
	{
		try
		{
			StringBuilder updateQuerySb = new StringBuilder("UPDATE pre_sub_account_master ");
			updateQuerySb.append("SET is_account_no_created = ? ");
			updateQuerySb.append("WHERE mobile_no = ? ");
			updateQuerySb.append("AND account_type = ? ");			
			
			return this.jdbcTemplate.update(updateQuerySb.toString(),
					new Object[] 
					{
						preSubAccountMaster.getStrIsAccountNoCreated(),
						preSubAccountMaster.getStrMobileNo(), 
						preSubAccountMaster.getStrAccountType()
					});
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	
	@Override
	public List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster) {
		{
			String regPendingCustWithLinkedAcc = ""
					+"SELECT cm.cust_id AS strCustId, pasm.created_date AS strDateOfRegistration, pasm.account_type AS strAccountType, "
					+ "atm.description AS strDescription, DATEDIFF(CURDATE(),created_date) AS ageing "
					+ "From pre_sub_account_master pasm "
					+ "INNER JOIN account_type_master atm "
					+ "INNER JOIN customer_master cm "
					+ "ON atm.account_type = pasm.account_type "
					+ "WHERE created_date BETWEEN ? AND ? AND pasm.is_account_no_created = 'N' ";
			
			List<PreSubAccountMaster> getRegCustWithLinkAccount = jdbcTemplate.query(regPendingCustWithLinkedAcc,
					new BeanPropertyRowMapper<PreSubAccountMaster>(PreSubAccountMaster.class),
					new Object[] { 
							preSubAccountMaster.getFromDate(),
							preSubAccountMaster.getToDate()
							//preSubAccountMaster.getStrIsAccountNoCreated() 
							 
							}
					);
			return getRegCustWithLinkAccount;
		}
	}
}
