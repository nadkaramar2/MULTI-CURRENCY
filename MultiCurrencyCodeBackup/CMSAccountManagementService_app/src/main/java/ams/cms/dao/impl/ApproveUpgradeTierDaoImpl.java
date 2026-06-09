package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ApproveUpgradeTierDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.ApproveUpgradeTier;

@Repository
public class ApproveUpgradeTierDaoImpl extends AbstractGenericDao<ApproveUpgradeTier> implements ApproveUpgradeTierDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ApproveUpgradeTierDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<ApproveUpgradeTier> getUpgradeTierType(ApproveUpgradeTier approveUpgradeTier) 
	{
		try {
		String approveUpgradeTiers = " "
				+ "SELECT utrr.cust_id AS strCustId, "
				+ "(SELECT CONCAT_WS(' ', cm.first_name, cm.middle_name, cm.last_name ) AS strCustName from customer_master cm WHERE cm.cust_id = utrr.cust_id) AS strCustName, "
				+ "utrr.tier_type AS strTierType FROM " 
				+ "upgrade_tier_req_res utrr "
				+ "INNER JOIN customer_master cm " 
				+ "ON cm.cust_id = utrr.cust_id "
				+ "WHERE utrr.req_status = 'pending' ";
				if (!"all".equalsIgnoreCase(approveUpgradeTier.getStrTierType()))
				{
					approveUpgradeTiers = approveUpgradeTiers +"AND utrr.tier_type = '"+approveUpgradeTier.getStrTierType()+"' ";
					
				}
				System.out.println("ApproveUpgradeTierDaoImpl.getUpgradeTierType()"+approveUpgradeTiers);
				List<ApproveUpgradeTier> getApproveUpgradeTier = jdbcTemplate.query(approveUpgradeTiers,
				new BeanPropertyRowMapper<ApproveUpgradeTier>(ApproveUpgradeTier.class),
				new Object[] {});
		return getApproveUpgradeTier;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}

	@Override
	public List<AccountTypeTierBasedLimit> getAccountTypeTierBasedLimit(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		try
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT attbl.daily_cumulative_txn_limit as strMaxAssignDailyCumulativeLimit, attbl.cumulative_balance_limit AS strCumulativeBalanceLimit,");
			
			if ("Tier1".equalsIgnoreCase(accountTypeTierBasedLimit.getStrTierType()))
			{
				queryBuilder.append(" tam.tier1_daily_cum_limit AS strDailyCumulativeTxnLimit,");
				queryBuilder.append(" tam.available_tier1_daily_cum_limit AS strAvailableDailyCumulativeLimit");
			}
			else if ("Tier2".equalsIgnoreCase(accountTypeTierBasedLimit.getStrTierType())) 
			{
				queryBuilder.append(" tam.tier2_daily_cum_limit AS strDailyCumulativeTxnLimit,");
				queryBuilder.append(" tam.available_tier2_daily_cum_limit AS strAvailableDailyCumulativeLimit");	
			}
			else if ("Tier3".equalsIgnoreCase(accountTypeTierBasedLimit.getStrTierType())) 
			{
				queryBuilder.append(" tam.tier3_daily_cum_limit AS strDailyCumulativeTxnLimit,");
				queryBuilder.append(" tam.available_tier3_daily_cum_limit AS strAvailableDailyCumulativeLimit");	
			}
			
			queryBuilder.append(" FROM customer_master cm INNER JOIN tier_account_master tam");
			queryBuilder.append(" ON tam.cust_id = cm.cust_id INNER JOIN account_type_tier_based_limit attbl");	
			
			queryBuilder.append(" ON attbl.account_type = tam.account_type WHERE tam.account_no = '"+accountTypeTierBasedLimit.getStrAccountNo()+"'");
			queryBuilder.append(" AND attbl.tier_type = '"+accountTypeTierBasedLimit.getStrTierType()+"' AND tam.cust_id= '"+accountTypeTierBasedLimit.getStrCustId()+"'");	
	
			System.out.println(queryBuilder.toString());

			List<AccountTypeTierBasedLimit> tierTypeList = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountTypeTierBasedLimit>(AccountTypeTierBasedLimit.class), new Object[]{});
			return tierTypeList;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
