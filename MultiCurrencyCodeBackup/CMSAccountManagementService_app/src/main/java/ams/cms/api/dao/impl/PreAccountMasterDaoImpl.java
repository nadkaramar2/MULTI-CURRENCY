package ams.cms.api.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.PreAccountMasterDao;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountKycDetails;
import ams.cms.services.AccountKycDetailsService;

@Repository
public class PreAccountMasterDaoImpl extends AbstractGenericDao<PreAccountMaster> implements PreAccountMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(PreAccountMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AccountKycDetailsService accountKycDetailsService;
	
	@SuppressWarnings("unchecked")
	@Override
	public PreAccountMaster getPreAccountInfo(String mobileNumber) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (!mobileNumber.isEmpty()) {
				criteria.add(Restrictions.eq("strMobileNo", mobileNumber));
			}
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			if (listData != null && listData.size() > 0) 
			{ 
				return listData.get(0); 
			}
			 
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PreAccountMaster getPreAccountMasterData(String mobileNumber,String emailId) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			
			if (mobileNumber != null && !mobileNumber.isEmpty())
			{
				criteria.add(Restrictions.eq("strMobileNo",mobileNumber));
			}
			if (emailId !=null && !emailId.isEmpty())
			{
				criteria.add(Restrictions.eq("strEmailID", emailId));
			}
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData.get(0);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isAccountExist(PreAccountMaster preAccountMaster) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (preAccountMaster.getStrMobileNo() != null && preAccountMaster.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo",preAccountMaster.getStrMobileNo()));
			}
			if (preAccountMaster.getStrEmailID() != null && preAccountMaster.getStrEmailID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strEmailID", preAccountMaster.getStrEmailID()));
			}
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			return listData.size() > 0;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
	//Added by Sunny Soni for login Start
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PreAccountMaster getPreAccountMasterBasedOnUserName(PreAccountMaster preAccountMaster)
	{
		try 
		{
			String sql = "SELECT id as strID, mobile_no AS strMobileNo, email_id AS strEmailID,"
					+ " password AS strPassword "
					+ "FROM pre_account_master where mobile_no = ?";
			List<PreAccountMaster> preAccountMasters = this.jdbcTemplate.query(sql,
					 (RowMapper) new BeanPropertyRowMapper(PreAccountMaster.class),
						new Object[] 
						{
							preAccountMaster.getStrMobileNo()
						} 
					);
			if(preAccountMasters!=null && preAccountMasters.size() >0 ) 
			{
				preAccountMaster = preAccountMasters.get(0);
			}
			return preAccountMaster;
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//Added by Sunny Soni for login End
	
	@Override
	public int updatePreAccountMasterBasedOnParameter(PreAccountMaster preAccountMaster) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE pre_account_master SET ");
			
			if (preAccountMaster.getStrJwtToken() != null && preAccountMaster.getStrJwtToken().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("jwtToken = ").append("?");
				objectList.add(preAccountMaster.getStrJwtToken());
			}
			if (preAccountMaster.getStrLastloginDate()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("last_login_date = ").append("?");
				objectList.add(preAccountMaster.getStrLastloginDate());
			}
			if (preAccountMaster.getStrPassword()!=null && preAccountMaster.getStrPassword().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("password = ").append("?");
				objectList.add(preAccountMaster.getStrPassword());
			}
			if (preAccountMaster.getStrAccountCreatedFlag() != null && preAccountMaster.getStrAccountCreatedFlag().trim().length() > 0) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("account_created_flag = ").append("?");
				objectList.add(preAccountMaster.getStrAccountCreatedFlag());					
			}
			if (preAccountMaster.getStrIsAccountNoCreated() != null && preAccountMaster.getStrIsAccountNoCreated().trim().length() > 0) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("is_account_no_created = ").append("?");
				objectList.add(preAccountMaster.getStrIsAccountNoCreated());					
			}
			if (preAccountMaster.getIsCustIdCreated() != null && preAccountMaster.getIsCustIdCreated().trim().length() > 0) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("is_cust_id_created = ").append("?");
				objectList.add(preAccountMaster.getIsCustIdCreated());					
			}
			if (preAccountMaster.getStrIsKycVerified() != null && preAccountMaster.getStrIsKycVerified().trim().length() > 0) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("is_kyc_verified = ").append("?");
				objectList.add(preAccountMaster.getStrIsKycVerified());					
			}
			if(preAccountMaster.getStrSessionId() != null && preAccountMaster.getStrSessionId().trim().length() > 0) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("session_id = ").append("?");
				objectList.add(preAccountMaster.getStrSessionId());
			}
			if(preAccountMaster.getStrDateOfCreation() != null) 
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("created_date = ").append("?");
				objectList.add(preAccountMaster.getStrDateOfCreation());
			}
			queryBuilder.append(" WHERE mobile_no = ").append("?");
			objectList.add(preAccountMaster.getStrMobileNo());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}
	
	public int updatePreAccountMaster(PreAccountMaster preAccountMaster, String otp) 
	{
		try
		{
			return this.jdbcTemplate.update("UPDATE pre_account_master SET otp = ? WHERE mobile_no = ? ",
					//+ "and email_id = ?",
					//new Object[] { otp, preAccountMaster.getStrMobileNo(), preAccountMaster.getStrEmailID() });
					new Object[] { otp, preAccountMaster.getStrMobileNo() });
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	public int updateAccountType(PreAccountMaster preAccountMaster, String accountType)
	{
		int i = this.jdbcTemplate.update(
				"UPDATE pre_account_master " + "SET " + "account_type = ? " + "	WHERE " + "mobile_no = ? and "
						+ "email_id = ?",
				new Object[] { accountType, preAccountMaster.getStrMobileNo(), preAccountMaster.getStrEmailID() });
		if(i != 0 ) {
			return 1;
		} else {
			return 0;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PreAccountMaster getKycDataForverification(PreAccountMaster preAccountMaster)
	{
		try 
		{
			String sql = "SELECT address_proof_document_type as strAddressProofDocumentType, "
					+ "address_proof_document_value AS strAddressDocumentValue, "
					+ "address_proof_image AS strAddressProofImage,"
					+" identity_proof_document_type AS strIdentityProofDocumentType, "
					+ "identity_proof_document_value AS strIdentityProofDocumentValue, "
					+" identity_proof_image AS strIdentityProofImage "
					+ "FROM pre_account_master";
			List<PreAccountMaster> preAccountMasters = this.jdbcTemplate.query(sql,
					 (RowMapper) new BeanPropertyRowMapper(PreAccountMaster.class),
						new Object[] {} 
					);
			if(preAccountMasters != null && preAccountMasters.size() >0 ) 
			{
				preAccountMaster = preAccountMasters.get(0);
			}
			return preAccountMaster;
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PreAccountMaster> getPreAccountData(PreAccountMaster preAccountMaster) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (preAccountMaster.getStrAccountType() != null && preAccountMaster.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", preAccountMaster.getStrAccountType()));
			}
			if (preAccountMaster.getStrMobileNo() != null && preAccountMaster.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", preAccountMaster.getStrMobileNo()));
			}
			if (preAccountMaster.getStrIsAccountNoCreated() != null && preAccountMaster.getStrIsAccountNoCreated().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strIsAccountNoCreated", preAccountMaster.getStrIsAccountNoCreated()));
			}
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			return listData;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PreAccountMaster getPreAccountMaster(PreAccountMaster preAccountMaster)
	{
		Criteria criteria = createEntityCriteria();
		if (preAccountMaster.getStrAccountType() != null && preAccountMaster.getStrAccountType().trim().length() > 0)
		{
			criteria.add(Restrictions.eq("strAccountType", preAccountMaster.getStrAccountType()));
		}
		if (preAccountMaster.getStrMobileNo() != null && preAccountMaster.getStrMobileNo().trim().length() > 0)
		{
			criteria.add(Restrictions.eq("strMobileNo", preAccountMaster.getStrMobileNo()));
		}
		List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
		if(listData!=null && listData.size() > 0)
		{
			preAccountMaster = listData.get(0);
		}
		return preAccountMaster;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PreAccountMaster getPreAccountMasterDataByMobileNumber(String mobileNumber) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			if (mobileNumber != null && mobileNumber.trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", mobileNumber));
			}			
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			if (listData != null && listData.size() > 0)
			{
				return listData.get(0);
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public int addressProofUpload(String imageName, String mobileNo) 
	{
		try
		{
			String sql = "UPDATE kyc_details SET address_proof_document_name = ?,address_proof_upload_date = ? where mobile_no=?";
			int update = this.jdbcTemplate.update(sql, new Object[] { imageName, new Date(), mobileNo });
			return update;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int updatePreAccountMaster(PreAccountMaster preAccountMaster) 
	{
		try 
		{
			/*
			int count = this.jdbcTemplate.update(
					"UPDATE pre_account_master SET is_account_no_created = ?, account_issue_date = ?"
					+ "WHERE " + "mobile_no = ?",
					new Object[] 
					{ 
							preAccountMaster.getStrIsAccountNoCreated(),
							new Timestamp(System.currentTimeMillis()),
							preAccountMaster.getStrMobileNo()
					});
			return count;
			*/
			
			int count = this.jdbcTemplate.update(
					"UPDATE pre_account_master SET is_cust_id_created = ?, account_issue_date = ?"
					+ "WHERE " + "mobile_no = ?",
					new Object[] 
					{ 
							preAccountMaster.getIsCustIdCreated(),
							new Timestamp(System.currentTimeMillis()),
							preAccountMaster.getStrMobileNo()
					});
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
/*	@SuppressWarnings("unchecked")
	@Override
	public PreAccountMaster getPreAccountInfo(String mobileNumber) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (!mobileNumber.isEmpty()) {
				criteria.add(Restrictions.eq("strMobileNo", mobileNumber));
			}
			
			  List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			  if (listData != null && listData.size() > 0) { return listData.get(0); }
			 
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
*/
	@Override
	public int updatePassword(PreAccountMaster preAccountMaster) 
	{
		try {
			String query = "update pre_account_master SET password = ? WHERE mobile_no = ? ";
			
			return this.jdbcTemplate.update(query,	
			new Object[]
			{ 
				preAccountMaster.getStrPassword(), 
				preAccountMaster.getStrMobileNo() 
			});
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEmaiExist(PreAccountMaster preAccountMaster)
	{
		try {
			Criteria criteria = createEntityCriteria();
			if (preAccountMaster.getStrEmailID() != null && preAccountMaster.getStrEmailID().trim().length() > 0) {
				criteria.add(Restrictions.eq("strEmailID", preAccountMaster.getStrEmailID()));
			}
			List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
			return listData.size() > 0;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
	// added by ankit
	@Override
	public int IdentityProofUpload(String imageName, String mobileNo)
	{
		try {
			String sql = "UPDATE kyc_details SET identity_proof_document_name = ?,identity_proof_upload_date = ? where mobile_no=?";
			int update = this.jdbcTemplate.update(sql, new Object[] { imageName, new Date(), mobileNo });
			return update;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AccountKycDetails getKycIdentityDocumentType(String mobileNo)
	{
		try
		{
			String sql = "select identity_proof_document_type as strIdentityProofDocumentType from kyc_details WHERE mobile_no = ?";
			AccountKycDetails accountKycDetails = null;
			List<AccountKycDetails> identityProofDocumentType = this.jdbcTemplate.query(sql,
					(RowMapper) new BeanPropertyRowMapper(AccountKycDetails.class), new Object[] { mobileNo });
			if (identityProofDocumentType != null && identityProofDocumentType.size() > 0) {
				accountKycDetails = identityProofDocumentType.get(0);
			}
			return accountKycDetails;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AccountKycDetails getKycAddressDocumentType(String mobileNo)
	{
		try 
		{
			String sql = "select address_proof_document_type as strAddressProofDocumentType from kyc_details WHERE mobile_no = ?";
			AccountKycDetails accountKycDetails = null;
			List<AccountKycDetails> adressProofDocumentType = this.jdbcTemplate.query(sql,
					(RowMapper) new BeanPropertyRowMapper(AccountKycDetails.class), 
					new Object[] 
					{
						mobileNo
					});
			if (adressProofDocumentType != null && adressProofDocumentType.size() > 0) 
			{
				accountKycDetails = adressProofDocumentType.get(0);
			}
			return accountKycDetails;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public int saveAccountKycDetails(PreAccountMaster accountKycDetails) 
	{
		int count = this.jdbcTemplate.update("INSERT INTO kyc_details (mobile_no,account_type) VALUES (?,?)",
				new Object[] { accountKycDetails.getStrMobileNo(), accountKycDetails.getStrAccountType() });
		return count;

	}

	public int updateSignUpdata(PreAccountMaster preAccountMaster) 
	{
		String sql = "UPDATE pre_account_master SET fname = ?, mname = ?, lname = ?, birth_date = ?, gender = ?, jwtToken = ?, created_date = ?, created_by = ? WHERE mobile_no = ? AND email_id = ?";
		int update = this.jdbcTemplate.update(sql,
				new Object[] { preAccountMaster.getStrFirstName(), preAccountMaster.getStrMiddleName(),
						preAccountMaster.getStrLastName(), preAccountMaster.getBirthDate(),
						preAccountMaster.getStrGender(), preAccountMaster.getStrJwtToken(),
						preAccountMaster.getStrDateOfCreation(), preAccountMaster.getStrCreatedBy(),
						preAccountMaster.getStrMobileNo(), preAccountMaster.getStrEmailID() });

		return update;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PreAccountMaster> getNonLinkedCustomerList(PreAccountMaster preAccountMaster) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append("SELECT pream.fname AS strFirstName, ");
			sqlQuery.append("pream.lname AS strLastName, ");
			sqlQuery.append("pream.mobile_no AS strMobileNo, ");
			sqlQuery.append("pream.email_id AS strEmailID,");
			sqlQuery.append("'NULL' AS strAccountType, ");
			sqlQuery.append("pream.is_kyc_verified AS strIsKycVerified, ");
			sqlQuery.append("'Y' AS strIsCustomerIdRelated, ");
			sqlQuery.append("'N' AS strIsAccountNoRelated, ");
			sqlQuery.append("'NA' AS cust_id ");
			sqlQuery.append("FROM pre_account_master pream ");
			sqlQuery.append("WHERE pream.is_cust_id_created = 'N' ");
			sqlQuery.append("AND pream.password IS NOT NULL");
			/*
			sqlQuery.append("INNER JOIN pre_sub_account_master presam ");
			sqlQuery.append("ON pream.mobile_no = presam.mobile_no ");
			sqlQuery.append("WHERE presam.is_cust_id_created = 'N' ");
			*/
			
			List<PreAccountMaster> preAccountMasters = this.jdbcTemplate.query(sqlQuery.toString(),
			(RowMapper) new BeanPropertyRowMapper(PreAccountMaster.class), new Object[] {});
			return preAccountMasters;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PreAccountMaster> getNonLinkedAccountNoList(PreAccountMaster preAccountMaster) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append("SELECT pream.fname AS strFirstName, ");
			sqlQuery.append("pream.lname AS strLastName, ");
			sqlQuery.append("pream.mobile_no AS strMobileNo, ");
			sqlQuery.append("presam.account_type AS strAccountType, ");
			sqlQuery.append("pream.is_kyc_verified AS strIsKycVerified, ");
			sqlQuery.append("'Y' AS strIsAccountNoRelated,");	
			sqlQuery.append("'N' AS strIsCustomerIdRelated, ");
			sqlQuery.append("custMast.cust_id AS cust_id, ");
			sqlQuery.append("atm.account_type_category AS accountTypeCategory ");
			sqlQuery.append("FROM pre_account_master pream ");
			sqlQuery.append("INNER JOIN pre_sub_account_master presam ");
			sqlQuery.append("ON pream.mobile_no = presam.mobile_no ");
			sqlQuery.append("INNER JOIN customer_master custMast ");
			sqlQuery.append("ON custMast.mobile_no = pream.mobile_no ");
			sqlQuery.append("INNER JOIN account_type_master atm ");
			sqlQuery.append("ON atm.account_type = presam.account_type ");
			sqlQuery.append("WHERE presam.is_account_no_created = 'N' ");
			sqlQuery.append("AND presam.account_type IS NOT NULL");
			
			List<PreAccountMaster> preAccountMasters = this.jdbcTemplate.query(sqlQuery.toString(),
				(RowMapper) new BeanPropertyRowMapper(PreAccountMaster.class),
				new Object[] 
				{
				}
			);
			return preAccountMasters;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PreAccountMaster getPreAccountMasterToken(String token)
	{
		PreAccountMaster preAccountMaster = new PreAccountMaster();
		Criteria criteria = createEntityCriteria();
		if (token != null && token.trim().length() > 0)
		{
			criteria.add(Restrictions.eq("strJwtToken", token));
		}
		
		List<PreAccountMaster> listData = (List<PreAccountMaster>) criteria.list();
		if(listData!=null && listData.size() > 0)
		{
			preAccountMaster = listData.get(0);
		}
		return preAccountMaster;
	}

	@Override
	public int updatePreAccountExistingInfoData(PreAccountMaster preAccountMaster)
	{
		int updateResult = 0;
		try 
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE pre_account_master SET ");
			updateQuery.append("fname = ?, mname = ?, lname = ?, birth_date = ?, ");
			updateQuery.append("gender = ?, jwtToken = ?, created_date = ?, created_by = ?, email_id = ?, ");
			updateQuery.append("country_code_shortname = ?, country_code = ?, is_cust_id_created = ?, is_kyc_verified = ? ");
			updateQuery.append("WHERE mobile_no = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[] 
			{ 
					preAccountMaster.getStrFirstName(), 
					preAccountMaster.getStrMiddleName(),
					preAccountMaster.getStrLastName(), 
					preAccountMaster.getBirthDate(),
					preAccountMaster.getStrGender(), 
					preAccountMaster.getStrJwtToken(),
					preAccountMaster.getStrDateOfCreation(), 
					preAccountMaster.getStrCreatedBy(),
					preAccountMaster.getStrEmailID(), 
					preAccountMaster.getStrCountryCodeShortName(),
					preAccountMaster.getStrCountryCode(),
					preAccountMaster.getIsCustIdCreated(),
					preAccountMaster.getStrIsKycVerified(),
					preAccountMaster.getStrMobileNo()
			});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updateResult;
	}

	@Override
	public String getCountryCode(String mobileNo)
	{
		try 
		{
			String sql = "SELECT pam.country_code as strCountryCode FROM pre_account_master pam where pam.mobile_no = ?";
			String countryCode = this.jdbcTemplate.queryForObject(sql, String.class, new Object[] {mobileNo} );
			if (countryCode!=null && countryCode.trim().length() > 0) 
			{
				countryCode = countryCode.trim();
			}
			return countryCode;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public int getUpdateMobileCustlist(PreAccountMaster preAccountMaster) 
	{
		int updateResult = 0;
		try 
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE pre_account_master SET ");
			updateQuery.append(" is_cust_id_created = 'Y', is_kyc_verified = 'Y'");
			updateQuery.append("WHERE mobile_no = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[] 
			{ 
					preAccountMaster.getIsCustIdCreated(),
					preAccountMaster.getStrIsKycVerified(),
					preAccountMaster.getStrMobileNo()
			});
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return updateResult;
	}

	@Override
	public int getUpdatePreAccountMasterKyclist(PreAccountMaster preAccountMaster) 
	{
		int updateResult = 0;
		try 
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE pre_account_master SET ");
			updateQuery.append("is_kyc_verified = 'Y'");
			updateQuery.append("WHERE mobile_no = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[] 
			{ 
					preAccountMaster.getStrIsKycVerified(),
					preAccountMaster.getStrMobileNo()
			});
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return updateResult;
	}
	
	@Override
	public List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster) {
	{
			String regPendingCustWithLinkedAcc = ""
					+" SELECT cm.cust_id AS strCustId, pasm.created_date AS strDateOfRegistration, pasm.account_type AS strAccountType, "
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

	@Override
	public List<PreAccountMaster> getRegisterCustomers(PreAccountMaster preAccountMaster) {
		try 
		{
	     	    StringBuilder queryBuilder = new StringBuilder("SELECT IFNULL(cm.cust_id, '-') AS cust_id, ");
				queryBuilder.append("IFNULL(pam.fname, '') AS strFirstName, IFNULL(pam.mname, '') AS strMiddleName, IFNULL(pam.lname, '') AS strLastName,");
				queryBuilder.append("DATE(pam.created_date) AS strDateofRegistration, TIME(pam.created_date) AS strTimeofRegistration, IFNULL(pam.is_cust_id_created, '') AS strAccountRegistered, "); 
				queryBuilder.append("DATEDIFF(CURDATE(),DATE(pam.created_date)) AS strAgeing FROM pre_account_master pam INNER JOIN customer_master cm ON pam.mobile_no = cm.mobile_no "); 
				queryBuilder.append("WHERE pam.created_date BETWEEN '"+preAccountMaster.getFromDate()+" 00:00:00' AND '"+preAccountMaster.getToDate()+" 23:59:59' AND pam.is_cust_id_created= '"+preAccountMaster.getStrStatus()+"' ");
					
	   	List<PreAccountMaster> accountStatementsList = jdbcTemplate.query(queryBuilder.toString(),
		new BeanPropertyRowMapper<PreAccountMaster>(PreAccountMaster.class),new Object[] {});
		return accountStatementsList;
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return null;
  }
//Add by Abhishek-End

	@Override
	public int addAddressOfCustomer(PreAccountMaster preAccountMaster) 
	{

		StringBuilder updateSb = new StringBuilder("UPDATE pre_account_master SET ");
		updateSb.append("address = ?, address2 = ?, address3 = ?, state = ?,");
		updateSb.append("title = ?, pincode = ?, city = ?, country = ? ");
		updateSb.append("WHERE mobile_no = ?");
		
		int count = this.jdbcTemplate.update(updateSb.toString(),
				new Object[] 
				{ 
						preAccountMaster.getAddress(),
						preAccountMaster.getAddress2(),
						preAccountMaster.getAddress3(),
						preAccountMaster.getState(),
						preAccountMaster.getTitle(),
						preAccountMaster.getPincode(),
						preAccountMaster.getCity(),
						preAccountMaster.getCountry(),
						preAccountMaster.getStrMobileNo()
				});
		return count;
	}
	
	//added by ankit on 09-05-2023
		@Override
		public int saveTier1PassportPhoto(String saveImageName, String mobileNo) 
		{
			try 
			{
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrMobileNo(mobileNo);
				accountKycDetails = accountKycDetailsService.getSingleAccountKycDetail(accountKycDetails);
				amsLogger.writeInfoLog("accountKycDetails:::"+accountKycDetails);
				String sql = "";
				if (accountKycDetails!=null && accountKycDetails.getStrID()!=null) 
				{
					sql = "UPDATE kyc_details SET tier1_passport_photograph = ? where mobile_no = ?";
				}
				else
				{
					sql = "INSERT INTO kyc_details (tier1_passport_photograph, mobile_no) VALUES (?, ?)";
				}
				int count = this.jdbcTemplate.update(sql, new Object[] { saveImageName,mobileNo });
				amsLogger.writeInfoLog("count::"+count);
				return count;
			} catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			return 0;
		}
		
		@Override
		public int saveTier2PassportPhoto(String saveImageName, String mobileNo) {
			try {
				String sql = "UPDATE kyc_details SET tier2_passport_photograph = ? where mobile_no=?";
				int update = this.jdbcTemplate.update(sql, new Object[] { saveImageName,mobileNo });
				return update;
			} catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			return 0;
		}
		//added by ankit on 09-05-2023

		// not working dont know why 
		@Override
		public int addBvnNo(PreAccountMaster preAccountMaster) 
		{
			String cust_id = preAccountMaster.getCust_id();
			try {
				String sql = "UPDATE kyc_details SET bvn_no = ? where cust_id=?";
				int update = this.jdbcTemplate.update(sql,
						new Object[] { 
								preAccountMaster.getStrBvnNo(),
								cust_id
								});
				System.out.println(preAccountMaster.getStrBvnNo()+" "+preAccountMaster.getCust_id());
				return update;
				
			} catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			return 0;
		}
		
		@Override
		public List<PreAccountMaster> checkBvnAndGetCustId(PreAccountMaster preAccountMaster)
		{
			try 
			{
				String sql = "SELECT cust_id as cust_id from kyc_details WHERE bvn_no = ? ";
				List<PreAccountMaster> preAccountMasters = jdbcTemplate.query(sql, new BeanPropertyRowMapper<PreAccountMaster>(PreAccountMaster.class),
						new Object[] {preAccountMaster.getStrBvnNo()});
			return preAccountMasters;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	  }
}
