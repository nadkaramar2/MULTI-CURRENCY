package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.BillingCycleModelDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.BillingCycleModel;

@Repository
public class BillingCycleMasterDaoImpl extends AbstractGenericDao<BillingCycleModel> implements BillingCycleModelDao 
{
  
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<BillingCycleModel> getBillingStatementlist(BillingCycleModel billingCycleModel) 
	{
		String sql = "SELECT id AS strID,participant_id AS strParticipantID, "
				+ "account_type AS strAccountType,account_number AS strAccountNumber, "
				+ "card_number AS strCardNumber,card_type AS strCardType, "
				+ "card_status AS strCardStatus,account_status AS strAccountStatus, "
				+ "creation_date AS strCreationDate, "
				+ "created_by AS strCreatedBy from billing_cycle_master AS link "
				+ "where link.account_type = ? and link.account_number = ? ";
		
		List<BillingCycleModel> getBillingCycleData = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<BillingCycleModel>(BillingCycleModel.class),
				new Object[] 
				{ });
			return getBillingCycleData;
	}
}
