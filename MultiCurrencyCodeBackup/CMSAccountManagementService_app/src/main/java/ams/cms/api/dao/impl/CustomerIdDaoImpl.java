package ams.cms.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.CustomerIdDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;

@Repository
public class CustomerIdDaoImpl extends AbstractGenericDao<CustomerIdCreation> implements CustomerIdDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CustomerIdDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public String getCustIdExist(CustomerIdCreation customerIdCreation) {
		try
		{
			String sql = "SELECT cust_id FROM customer_master WHERE mobile_no  = '"+customerIdCreation.getStrMobileNo()+"'";
			amsLogger.writeInfoLog("getCustIdExist:: sql =["+sql+"]");
			String custIdExist = jdbcTemplate.queryForObject(sql, String.class,	new Object[] {});
			return custIdExist;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	
	@Override
	public int updateAccountTypeBasedOnMobileNo(CustomerIdCreation customerIdCreation) {
		int i = this.jdbcTemplate.update(
				"UPDATE pre_account_master " + "SET " + "account_type = ? , is_account_no_created = 'N' " 
		    	+ "WHERE mobile_no = ?",
				new Object[] {customerIdCreation.getStrMobileNo() });
		if (i != 0) {
			return 1;
		} else {
			return 0;
		}
	
}
	@Override
	public String getIsAccNoCreated(CustomerIdCreation customerIdCreation, String accountType) 
	{
		try
		{			
			/*String sql = "SELECT pam.is_account_no_created FROM customer_master AS cm "
			+ "INNER JOIN pre_account_master AS pam " 
			+ "ON cm.mobile_no = pam.mobile_no "
			+ "WHERE cm.cust_id = ? AND pam.account_type = ? ";*/
		
			StringBuilder selectQueySb = new StringBuilder("SELECT psam.is_account_no_created FROM customer_master AS cm ");
			selectQueySb.append("INNER JOIN pre_sub_account_master AS psam ");
			selectQueySb.append("ON cm.mobile_no = psam.mobile_no ");
			selectQueySb.append("WHERE cm.cust_id = ? AND psam.account_type = ?");
		
			String getIsAccNoCreated = jdbcTemplate.queryForObject(selectQueySb.toString(), 
					String.class,
					new Object[] 
					{ 
						customerIdCreation.getStrCustId(), accountType 
					});
			return getIsAccNoCreated;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		
		return null;
	}
	@Override
	public String getMobileNoBasedOnCustId(CustomerIdCreation customerIdCreation) {
		try
		{
			String sql = "SELECT pam.mobile_no FROM customer_master AS cm INNER JOIN pre_account_master "
					+ " AS pam ON cm.mobile_no = pam.mobile_no WHERE cm.cust_id = ? ";
			String getMobileNo = jdbcTemplate.queryForObject(sql, String.class, 
					new Object[] 
					{ 
						customerIdCreation.getStrCustId()  
					});
			return getMobileNo;
		}catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerIdCreation> getCustomerPinVerifly(CustomerIdCreation customerIdCreation) 
	{
		try 
		  {
			Criteria criteria = createEntityCriteria();
			{
			if(customerIdCreation.getStrCustId() != null && customerIdCreation.getStrCustId().trim().length() > 0)
			   criteria.add(Restrictions.eq("cust_id",customerIdCreation.getStrCustId()));
			}
			if(customerIdCreation.getStrPin() != null && customerIdCreation.getStrPin().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("pin",customerIdCreation.getStrPin()));
			}
		
			List<CustomerIdCreation> ConfirmPin = (List<CustomerIdCreation>) criteria.list();
			if (ConfirmPin !=null && ConfirmPin.size() > 0) 
			{
				return ConfirmPin;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	@Override
	public int updatePinAgainstCustId(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			String query = "update customer_master SET pin = ? WHERE cust_id = ? ";			
			return this.jdbcTemplate.update(query,	
			new Object[]
			{ 
				customerIdCreation.getStrNewPin(), 
				customerIdCreation.getStrCustId() 
			});
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public String getCustomerPIN(CustomerIdCreation customerIdCreation)
	{
		try 
		{
			String sql = "SELECT pin AS strPin FROM customer_master WHERE cust_id= ?";
			String custIdinfo = jdbcTemplate.queryForObject(sql, String.class, new Object[] {
			   customerIdCreation.getStrCustId() 
			});
			return custIdinfo;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}


	@Override
	public String getCustomerMactchlist(CustomerIdCreation customerIdCreation) 
	{
		String emailId = null;
		try {
			String sql = "SELECT email AS strEmailID FROM customer_master WHERE cust_id= ?";
			emailId = jdbcTemplate.queryForObject(sql, String.class,
					new Object[] 
			        {
					   customerIdCreation.getStrCustId() 
					});
			return emailId;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	@Override
	public int updateOtpAgainstCustId(CustomerIdCreation customerIdCreation) {
		try 
		{
			/*String query = "update customer_master SET pin_otp = ? WHERE cust_id = ? ";
			return this.jdbcTemplate.update(query,	
					new Object[]
					{ 
						customerIdCreation.getStrPinOTP(), 
						customerIdCreation.getStrCustId() 
					});
				*/
			
			List<Object> objectList = new ArrayList<Object>();
			StringBuilder queryBuilder = new StringBuilder("update customer_master SET ");
			
			if (customerIdCreation.getStrTxnOTP() != null && customerIdCreation.getStrTxnOTP().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("txn_otp = ").append("?");
				objectList.add(customerIdCreation.getStrTxnOTP());
			}
			if (customerIdCreation.getStrPinOTP() != null && customerIdCreation.getStrPinOTP().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("pin_otp = ").append("?");
				objectList.add(customerIdCreation.getStrPinOTP());
			}
			
			queryBuilder.append(" WHERE cust_id = ").append("?");
			objectList.add(customerIdCreation.getStrCustId());
					
			Object[] object = objectList.toArray();		
			
			int count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;		
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}


	@Override
	public String getCustomerPinOTP(CustomerIdCreation customerIdCreation) {
		try {
			String sql = "SELECT pin_otp AS strPinOTP FROM customer_master WHERE cust_id= ?";
			String pinOTP = jdbcTemplate.queryForObject(sql, String.class,
					new Object[] 
			        {
					   customerIdCreation.getStrCustId() 
					});
			return pinOTP;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<CustomerIdCreation> getEmailOfCustomer(CustomerIdCreation customerIdCreation)
	{
		try
		{
			String sql = "SELECT email AS strEmailID FROM customer_master WHERE cust_id= ?";
			List<CustomerIdCreation> query = jdbcTemplate.query(sql,
					(RowMapper<CustomerIdCreation>) new BeanPropertyRowMapper<CustomerIdCreation>(CustomerIdCreation.class),
					new Object[] 
			        {
					   customerIdCreation.getStrCustId() 
					});
			return query;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	//Added by Pankaj Pawar Start
	@Override
	public String getTransactionPinOTP(CustomerIdCreation customerIdCreation)
	{
		try
		{
			String sql = "SELECT txn_otp AS strPinOTP FROM customer_master WHERE cust_id= ? ";
			String pinOTP = jdbcTemplate.queryForObject(sql, String.class,
			new Object[] 
	        {
			   customerIdCreation.getStrCustId(),
			});
			return pinOTP;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//Added by Pankaj Pawar End


	@Override
	public CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strCustomerName ");
			queryBuilder.append("FROM customer_master AS am ");			
			queryBuilder.append("WHERE am.cust_id = '"+customerIdCreation.getStrCustId()+"'");
			
			amsLogger.writeInfoLog("getCustomerInformationByCustId:: query=["+queryBuilder.toString()+"]");
			
			List<CustomerIdCreation> customerIdCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<CustomerIdCreation>(CustomerIdCreation.class), new Object[]  {});
			
			if (customerIdCreations!=null && customerIdCreations.size() > 0) 
			{
				return customerIdCreations.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CustomerIdCreation getCustIdAgentsActiveTier(CustomerIdCreation customerIdCreation) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if(customerIdCreation.getStrCustId() != null && customerIdCreation.getStrCustId().trim().length() > 0) {
			   criteria.add(Restrictions.eq("strCustId",customerIdCreation.getStrCustId()));
			}
			if(customerIdCreation.getStrMobileNo() != null && customerIdCreation.getStrMobileNo().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strMobileNo",customerIdCreation.getStrMobileNo()));
			}
		
			List<CustomerIdCreation> CustIdCheck = (List<CustomerIdCreation>) criteria.list();
			if (CustIdCheck !=null && CustIdCheck.size() > 0) 
			{
				return CustIdCheck.get(0);
			}
	}
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
		return null;
	}
}
