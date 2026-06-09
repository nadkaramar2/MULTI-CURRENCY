package ams.cms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CustomerIDCreationDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdTable;

@Repository
public class CustomerIDCreationImpl extends AbstractGenericDao<CustomerIdCreation> implements CustomerIDCreationDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CustomerIDCreationImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	@Override
	public CustomerIdCreation getSingleAccountKycDetail(CustomerIdCreation customerIdCreation) {
		return null;
	}

	@Override
	public String getCustomerId() 
	{
		String customerID = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) {
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) {
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			String Date = dateFormat.format(new Date());
			   
			return customerID;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String getCustid(String year, String julianDate)
	{
		try 
		{
			String sql = "SELECT last_txn_serial_no AS custId from customer_id_table where year = '"+year+"' AND julian_date = '"+julianDate+"'";
			
			List<String> strLst = this.jdbcTemplate.query(sql, new RowMapper<String>() 
			{
			    public String mapRow(ResultSet rs, int rowNum) throws SQLException
			    {
			        return rs.getString(1);
			    }
			});
			
			if (strLst.isEmpty()) 
			{
			    return "";
			} 
			else if (strLst.size() == 1) 
			{ 
			    return strLst.get(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateCustid(CustomerIdCreation customerIdCreation) 
	{
		String insertQuery = "INSERT INTO customer_master (cust_id,first_name,last_name,address,country,city,pincode) VALUES (?,?,?,?,?,?,?)";
		int count = this.jdbcTemplate.update(insertQuery, new Object[] 
		{ 
			customerIdCreation.getStrCustId(), 
			customerIdCreation.getStrFirstName(),
			customerIdCreation.getStrLastName(),
			customerIdCreation.getStrAddress1(),
			customerIdCreation.getStrCountry(),
			customerIdCreation.getStrCity(),
			customerIdCreation.getStrPinCode()
		});
		return count;
	}

	@Override
	public int updateCustIdTable(CustomerIdTable customerIdTable) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public  List<CustomerIdCreation> getcustomerdetailsbyId(CustomerIdCreation customerIdCreation)
	{
		try 
		{
			String id = customerIdCreation.getStrCustId();			
			if (id!=null && id.trim().length() > 0) 
			{
				Criteria criteria = createEntityCriteria();
				criteria.add(Restrictions.eq("strCustId", id.trim()));
				
				List<CustomerIdCreation> listData = (List<CustomerIdCreation>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData;
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountInformationListById::"+e);
			e.printStackTrace();
		}
		return null;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomerIdCreation getCustomerIdInfo(CustomerIdCreation customerIdCreation)
	{
		boolean isCriteriaRun = false; 
		try 
		{
			Criteria criteria = createEntityCriteria();			
			if (customerIdCreation.getStrCustId()!=null && customerIdCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", customerIdCreation.getStrCustId().trim()));
				isCriteriaRun = true;
			}
			if (customerIdCreation.getStrMobileNo()!=null && customerIdCreation.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", customerIdCreation.getStrMobileNo().trim()));
				isCriteriaRun = true;
			}
			if (isCriteriaRun) 
			{
				List<CustomerIdCreation> listData = (List<CustomerIdCreation>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getCustomerIdInfo::"+e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CustomerIdCreation getCustomerIdInfoByMobile(String mobileNumber) 
	{
		try
		{
			if (mobileNumber!=null && mobileNumber.trim().length() > 0) 
			{
				Criteria criteria = createEntityCriteria();
				criteria.add(Restrictions.eq("strMobileNo", mobileNumber.trim()));
				
				List<CustomerIdCreation> listData = (List<CustomerIdCreation>) criteria.list();
				if (listData != null && listData.size() > 0) 
				{
					return listData.get(0);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getCustomerIdInfoByMobile::"+e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerIdCreation> getCustomerAccountInfo(CustomerIdCreation customerIdCreation) {
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (customerIdCreation.getStrCustId()!=null && customerIdCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", customerIdCreation.getStrCustId().trim()));
			}
			
			List<CustomerIdCreation> listData = (List<CustomerIdCreation>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("CustomerIDCreationImpl.getCustomerAccountInfo()"+e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerIdCreation> getCustomerAccountDetailsBasedOnCustId(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			boolean isCriteriaRun = false;
			
			Criteria criteria = createEntityCriteria();
			if (customerIdCreation.getStrCustId() != null && customerIdCreation.getStrCustId().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strCustId", customerIdCreation.getStrCustId()));
				isCriteriaRun = true;
			}
			if (customerIdCreation.getStrMobileNo() != null && customerIdCreation.getStrMobileNo().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("strMobileNo", customerIdCreation.getStrMobileNo()));
				isCriteriaRun = true;
			}
			if (isCriteriaRun) 
			{
				List<CustomerIdCreation> listData = (List<CustomerIdCreation>) criteria.list();				
				if (listData !=null && listData.size() > 0) 
				{
					return listData;
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getCustomerAccountDetailsBasedOnCustId::"+e);
		}
		return null;
	}

	@Override
	public int updateCustomerAccountDetails(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE customer_master "
					+ "SET "
					+ "title = ? ,  "
					+ "first_name = ? , "
					+ "middle_name = ? , "
					+ "last_name = ? , "
					
					+ "gender = ? , "
					+ "dob = ? , "
					+ "email = ? , "
					+ "country_code = ? , "
					+ "mobile_no = ? , " 
					
					+ "phone_no = ? , "
					+ "address = ? , "
					+ "pincode = ? , "
					+ "country = ? , "
					+ "state = ? ,  "
					+ "city = ? "				
					
					+ "WHERE "
					+ "participant_id = ? and "
					+ "cust_id = ? ",
					new Object[] 
					{ 	customerIdCreation.getStrTitle(), 
				        customerIdCreation.getStrFirstName(), 
				        customerIdCreation.getStrMiddleName(), 
				        customerIdCreation.getStrLastName(),
				        
				        customerIdCreation.getStrGender(),
				        customerIdCreation.getStrDOB(),
				        customerIdCreation.getStrEmailID(),
				        customerIdCreation.getStrPhoneCode(),
				        
				        customerIdCreation.getStrMobileNo(),
				        customerIdCreation.getStrPhoneNo(),
				        customerIdCreation.getStrAddress1(),
				        
				        customerIdCreation.getStrPinCode(),
				        
				        customerIdCreation.getStrCountry(),
				        customerIdCreation.getStrState(),
				        customerIdCreation.getStrCity(),									
				        
						customerIdCreation.getStrParticipantID(), 
						customerIdCreation.getStrCustId() 
					});
				amsLogger.writeInfoLog("CustomerIDCreationImpl.updateCustomerAccountDetails()"+customerIdCreation); 
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getActiveTier(String strMobileNo) {
		try 
		{
			String selectQuery = "select active_tier from customer_master where mobile_no = ?";
			String activeTier = this.jdbcTemplate.queryForObject(selectQuery, String.class, new Object[] 
			{ 
				strMobileNo 
			});
			
			amsLogger.writeInfoLog("Get ActiveTier activeTier::" + activeTier);
			return activeTier;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateCustomerAccountDetailsBasedOnParameter(CustomerIdCreation customerIdCreation) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE customer_master SET ");
			
			if (customerIdCreation.getStrActiveTier() != null && customerIdCreation.getStrActiveTier().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("active_tier = ").append("?");
				objectList.add(customerIdCreation.getStrActiveTier());
			}
			if (customerIdCreation.getStrActiveTierDate()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("active_tier_date = ").append("?");
				objectList.add(customerIdCreation.getStrActiveTierDate());
			}
			if (customerIdCreation.getStrActiveTierTime()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("active_tier_time = ").append("?");
				objectList.add(customerIdCreation.getStrActiveTierTime());
			}
			if (customerIdCreation.getStrTier1Status() != null && customerIdCreation.getStrTier1Status().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier1_status = ").append("?");
				objectList.add(customerIdCreation.getStrTier1Status());
			}
			if (customerIdCreation.getStrTier2Status() != null && customerIdCreation.getStrTier2Status().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier2_status = ").append("?");
				objectList.add(customerIdCreation.getStrTier2Status());
			}
			if (customerIdCreation.getStrTier3Status() != null && customerIdCreation.getStrTier3Status().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier3_status = ").append("?");
				objectList.add(customerIdCreation.getStrTier3Status());
			}
			if (customerIdCreation.getStrTier1Date()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier1_date = ").append("?");
				objectList.add(customerIdCreation.getStrTier1Date());
			}
			if (customerIdCreation.getStrTier2Date()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier2_date = ").append("?");
				objectList.add(customerIdCreation.getStrTier2Date());
			}
			if (customerIdCreation.getStrTier3Date()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier3_date = ").append("?");
				objectList.add(customerIdCreation.getStrTier3Date());
			}
			if (customerIdCreation.getStrTier1Time()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier1_time = ").append("?");
				objectList.add(customerIdCreation.getStrTier1Time());
			}
			if (customerIdCreation.getStrTier2Time()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier2_time = ").append("?");
				objectList.add(customerIdCreation.getStrTier2Time());
			}
			if (customerIdCreation.getStrTier3Time()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("tier3_time = ").append("?");
				objectList.add(customerIdCreation.getStrTier3Time());
			}
			
			queryBuilder.append(" WHERE cust_id = ").append("?");
			objectList.add(customerIdCreation.getStrCustId());
			
			amsLogger.writeInfoLog("updateCustomerAccountDetailsBasedOnParameter queryBuilder::" + queryBuilder.toString());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}
}
