package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountResponse;
import ams.cms.api.model.AccountTxnBalanceRequest;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.utility.AccountMasterResponse;
import ams.cms.utility.Utils;

@Repository
public class AccountMasterDaoImpl extends AbstractGenericDao<AccountCreation> implements AccountMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountMasterDaoImpl.class);
	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public AccountCreation getAccountInformation(AccountCreation accountCreation) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (accountCreation.getStrParticipantID()!=null && accountCreation.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountCreation.getStrParticipantID()));
			}
			if (accountCreation.getStrAccountType()!=null && accountCreation.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountCreation.getStrAccountType()));
			}
			if (accountCreation.getStrAccountNumber()!=null && accountCreation.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", accountCreation.getStrAccountNumber()));
			}
			if (accountCreation.getStrMobileNo()!=null && accountCreation.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", accountCreation.getStrMobileNo()));
			}
			if (accountCreation.getStrCustId()!=null && accountCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", accountCreation.getStrCustId()));
			}
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
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
	public List<AccountCreation> getAccountInformationList(AccountCreation accountCreation) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (accountCreation.getStrParticipantID()!=null && accountCreation.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountCreation.getStrParticipantID()));
			}
			if (accountCreation.getStrAccountType()!=null && accountCreation.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountCreation.getStrAccountType()));
			}
			if (accountCreation.getStrAccountNumber()!=null && accountCreation.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", accountCreation.getStrAccountNumber()));
			}
			if (accountCreation.getStrCustId()!=null && accountCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", accountCreation.getStrCustId()));
			}
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
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
	public List<AccountCreation> getAccountInformationListById(String id)
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strID", id));
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
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
	public List<AccountCreation> getAccountInformationListByTypes(AccountCreation accountCreation)
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			
			if (accountCreation.getStrParticipantID()!=null && accountCreation.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountCreation.getStrParticipantID()));
			}
			if (accountCreation.getStrIsInstantAccount()!=null && accountCreation.getStrIsInstantAccount().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strIsInstantAccount", accountCreation.getStrIsInstantAccount()));
			}
			if (accountCreation.getStrAccountType()!=null && accountCreation.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountCreation.getStrAccountType()));
			}
			if (accountCreation.getStrMobileNo()!=null && accountCreation.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", accountCreation.getStrMobileNo()));
			}
			if (accountCreation.getStrCustId()!=null && accountCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", accountCreation.getStrCustId()));
			}
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public int updateAccountCreationFromInstanceAccount(AccountCreation accountCreation) 
	{
		try
		{
			long mobileNo = Long.parseLong( ((accountCreation.getStrMobileNo() != null && accountCreation.getStrMobileNo().length() > 0) ? accountCreation.getStrMobileNo() :  "0") );
			long phoneNo = Long.parseLong( ((accountCreation.getStrPhoneNo() != null && accountCreation.getStrPhoneNo().length() > 0 ) ?  accountCreation.getStrPhoneNo() : "0") );
			
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "title = ?, "
					+ "first_name = ?, "
					+ "middle_name = ?, "
					+ "last_name = ?, "
					+ "gender = ? , "
					+ "dob = ? , "
					
					+ "email = ?, "
					+ "mobile_no = ?, "
					+ "address1 = ?,  "
					+ "address2 = ?,  "
					+ "address3 = ?,  "
					+ "pincode = ?,  "
					
					+ "phone_no = ?,  "
					+ "is_instant_account = ?, "					
					+ "city = ?, "
					+ "state = ?, "
					+ "country = ?, "
					+ "opening_balance = ?, "
					
					+ "credit_limit_category = ?, "
					+ "available_credit_limit = ?, "
					+ "closing_balance = ?, "
					+ "load_count = ?, "
					+ "available_daily_limit = ?, "
					+ "available_monthly_limit = ?, "
					
					+ "available_yearly_limit = ?, "					
					+ "status = ?, "
					+ "created_by = ?"
					+ "	WHERE "
					/* + "id = ? and " */
					+ "account_type = ? and "
					+ "account_number = ?",
					new Object[] { accountCreation.getStrTitle(), 
									accountCreation.getStrFirstName(), 
									accountCreation.getStrMiddleName(),
									accountCreation.getStrLastName(), 
									accountCreation.getStrGender(), 
									
									accountCreation.getStrDOB(), 
									accountCreation.getStrEmailID(), 
									mobileNo, 
									accountCreation.getStrAddress1(), 
									accountCreation.getStrAddress2(),
									
									accountCreation.getStrAddress3(), 
									accountCreation.getStrPinCode(), 
									phoneNo, 
									"N", 
									accountCreation.getStrCity(),
									
									accountCreation.getStrState(),
									accountCreation.getStrCountry(),
									accountCreation.getStrOpeningBalance(),
									accountCreation.getStrCreditLimitCategory(),
									accountCreation.getStrAvailableCreditLimit(),
									
									accountCreation.getStrClosingBalance(),
									accountCreation.getStrLoadCount(),
									accountCreation.getStrAvailableDailyLimit(),
									accountCreation.getStrAvailableMonthlyLimit(),
									accountCreation.getStrAvailableYearlyLimit(),
									
									"Active", 
									accountCreation.getStrCreatedBy(),
									/* accountCreation.getStrID(), */
									accountCreation.getStrAccountType(), 
									accountCreation.getStrAccountNumber()
									});
			amsLogger.writeInfoLog("count" + count);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int updateAccountCreation(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "address1 = ?,  "
					+ "address2 = ?, "
					+ "address3 = ?, "
					+ "pincode = ?, "
					
					+ "city = ?, "
					+ "state = ?, "
					+ "country = ?, "					
					+ "status = ?, "
					
					+ "created_by = ? "					
					+ "WHERE "
					+ "id = ? and "
					+ "participant_id = ? and "
					+ "account_type = ? and "
					+ "account_number = ?",
					new Object[] { 	accountCreation.getStrAddress1(), 
									accountCreation.getStrAddress2(), 
									accountCreation.getStrAddress3(), 
									accountCreation.getStrPinCode(),
									
									accountCreation.getStrCity(),									
									accountCreation.getStrState(),
									accountCreation.getStrCountry(),									
									"Active", 
									
									accountCreation.getStrCreatedBy(),
									accountCreation.getStrID(), 
									accountCreation.getStrParticipantID(), 
									accountCreation.getStrAccountType(), 
									accountCreation.getStrAccountNumber() });
			amsLogger.writeInfoLog("ConfigurationDaoImpl.updateAccountCreation()" + accountCreation);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int[] batchEntryOfInstanAccount(List<AccountCreation> accountCreationlist)
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO account_master "
					+ "(participant_id, account_type, account_number, first_name, is_instant_account, "
					//+ "opening_balance, credit_limit_category, credit_limit_amount, available_credit_limit, tax_type) "
					//+ "opening_balance, credit_limit_category, available_credit_limit, tax_type) "
					+ "opening_balance, credit_limit_category, available_credit_limit) "
					//+ "values(?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter()
					+ "values(?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, accountCreationlist.get(i).getStrParticipantID());
							psmt.setString(2, accountCreationlist.get(i).getStrAccountType());
							psmt.setString(3, accountCreationlist.get(i).getStrAccountNumber());
							psmt.setString(4, accountCreationlist.get(i).getStrFirstName());
							psmt.setString(5, "Y");
							psmt.setString(6, accountCreationlist.get(i).getStrOpeningBalance());
							psmt.setString(7, accountCreationlist.get(i).getStrCreditLimitCategory());
							//psmt.setString(8, accountCreationlist.get(i).getStrCreditLimitAmount());
							psmt.setString(8, accountCreationlist.get(i).getStrAvailableCreditLimit());
							//psmt.setString(9, accountCreationlist.get(i).getStrTaxType());
						}
						
						@Override
						public int getBatchSize() {
							return accountCreationlist.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeInfoLog("Exception in batchEntryOfInstanAccount::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
	
	@Override
	public String getAvailableBalanceBasedOnAccountTypeAndNumber(AccountCreation accountCreation) 
	{
		try 
		{
			String sql = "SELECT closing_balance as strClosingBalance FROM account_master where account_type = ? AND account_number = ?";
			String closingBalance = this.jdbcTemplate.queryForObject(
					sql,
					String.class,
					new Object[]
					{
						accountCreation.getStrAccountType(), 
						accountCreation.getStrAccountNumber() 
					}
					);
			return closingBalance;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public int updateBalanceRelatedData(AccountCreation accountCreation)
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET opening_balance = ?, "
				+ "closing_balance = ?, "
				+ "load_count = ? "
				+ "where account_type = ? AND account_number = ?";
		try {
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{ 
					accountCreation.getStrOpeningBalance(), 
					accountCreation.getStrClosingBalance(),
					accountCreation.getStrLoadCount(), 
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			return count;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public List<AccountCreation> getAccountIssueData(AccountCreation accountCreation) 
	{
		String cardLinkedAccountIssueSearch = ""
				+ "SELECT id AS strID, account_type AS strAccountType, account_number AS strAccountNumber, "
				+ "title AS strTitle, first_name AS strFirstName, middle_name AS strMiddleName, "
				+ "last_name AS strLastName, gender AS strGender, dob AS strDOB, "
				+ "address1 AS strAddress1, address2 AS strAddress2, address3 AS strAddress3, pincode AS strPinCode, "
				+ "mobile_no AS strMobileNo, phone_no AS strPhoneNo, email AS strEmailID, "
				+ "city AS strCity, state AS strState, country AS strCountry, created_by AS strCreatedBy, "
				+ "status AS strStatus, is_instant_account As strIsInstantAccount "
				+ "from account_master WHERE Is_linked_with_card = ? ";
		List<AccountCreation> getAccountIssueData = jdbcTemplate.query(
				cardLinkedAccountIssueSearch,
				new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
				new Object[] { accountCreation.getStrIsLinkedwithCard()}
				//new Object[] { accountCreation.getStrIslinkedWithCard()}
				);

		return getAccountIssueData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AccountCreation getOutstandingAccount(AccountCreation accountCreation)
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			
			if (accountCreation.getStrAccountNumber()!=null && accountCreation.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", accountCreation.getStrAccountNumber()));
			}
			if (accountCreation.getStrCreditLimitCategory()!=null && accountCreation.getStrCreditLimitCategory().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCreditLimitCategory", accountCreation.getStrCreditLimitCategory()));
			}
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
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
    
	@Override
	public List<AccountCreation> getAccountProfilelist(AccountCreation accountCreation) 
	{
		try {
			  String statementQuery = "SELECT ams.participant_id AS strParticipantID,ams.account_type AS strAccountType,"
					+ "ams.account_number AS strAccountNumber,ams.title AS strTitle,ams.first_name AS strFirstName, "
					+ "ams.middle_name AS strMiddleName,ams.last_name AS strLastName,ams.email AS strEmailID, "
					+ "ams.mobile_no AS strMobileNo,ams.address1 AS strAddress1,ams.address2 AS strAddress2, "
					+ "ams.address3 AS strAddress3,ams.pincode AS strPinCode,ams.city AS strCity,ams.state AS strState, "
					+ "ams.creation_date AS strDateOfCreation,pams.account_issue_date AS strAccountIssueDate, "
					+ "pams.last_login_date AS strLastloginDate FROM account_master AS ams "
					+ "INNER JOIN pre_account_master AS pams ON ams.mobile_no=pams.mobile_no "
			        + "WHERE ams.account_number= ? AND ams.account_type = ? ";
			  List<AccountCreation> getAccountProfileData = jdbcTemplate.query( statementQuery,
						new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
						new Object[] { 
								
								
								accountCreation.getStrAccountNumber(),
								accountCreation.getStrAccountType(), 
						}
						);
		
		       return getAccountProfileData;
	   }
	catch (Exception e) 
	{
		amsLogger.writeInfoLog("Exception in updateAccountStatementlist::"+e);
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
	return null;
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AccountCreation> getCreditCardBalancelist(AccountCreation accountCreation) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if(accountCreation.getStrAccountNumber() != null && accountCreation.getStrAccountNumber().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountNumber",accountCreation.getStrAccountNumber()));
			}
			if(accountCreation.getStrAccountType() != null && accountCreation.getStrAccountType().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountType",accountCreation.getStrAccountType()));
			}
					
			List<AccountCreation>creditcardBalanceData= (List<AccountCreation>) criteria.list();
			if (creditcardBalanceData !=null && creditcardBalanceData.size() > 0) 
			{
				return creditcardBalanceData;
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountCreditcardBalance::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;
}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<AccountCreation> getCreditCardInterestlist(AccountCreation accountCreation) {
		try 
		{
			Criteria criteria = createEntityCriteria();
			if(accountCreation.getStrAccountNumber() != null && accountCreation.getStrAccountNumber().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountNumber",accountCreation.getStrAccountNumber()));
			}
			if(accountCreation.getStrAccountType() != null && accountCreation.getStrAccountType().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountType",accountCreation.getStrAccountType()));
			}
				
		
			List<AccountCreation>creditcardInterestData= (List<AccountCreation>) criteria.list();
			if (creditcardInterestData !=null && creditcardInterestData.size() > 0) 
			{
				return creditcardInterestData;
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountCreditcardBalance::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountCreation> getAccountBalanceView(AccountCreation accountCreation) {
		try 
		{
			Criteria criteria = createEntityCriteria();
			if(accountCreation.getStrAccountNumber() != null && accountCreation.getStrAccountNumber().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountNumber",accountCreation.getStrAccountNumber()));
			}
			if(accountCreation.getStrAccountType() != null && accountCreation.getStrAccountType().trim().length() > 0) 
			{
			   criteria.add(Restrictions.eq("strAccountType",accountCreation.getStrAccountType()));
			}
			
			List<AccountCreation> accountBalancelist= (List<AccountCreation>) criteria.list();
			if (accountBalancelist !=null && accountBalancelist.size() > 0) 
			{
				return accountBalancelist;
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountCreditcardBalance::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;	
	}

	@Override
	public List<AccountCreation> getAccountClosingBalance(AccountTxnBalanceRequest accountTxnBalanceRequest) {
		
		try 
		{
		   String statementQuery = "SELECT closing_balance AS strClosingBalance " 
		   		+"FROM account_master where account_number =? ";
            
			
			List<AccountCreation> accountTxnlist  = jdbcTemplate.query(statementQuery,
					new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[] 
			{ 
					accountTxnBalanceRequest.getStrAccountNumber()
			});
			return accountTxnlist;
		}
	catch (Exception e) 
	{
		amsLogger.writeInfoLog("Exception in AccountTransactionlist::"+e);
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
	return null;
	}

	@Override
	public int updateAccountAfterLinkedCard(AccountCreation accountCreation) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "Is_linked_with_card = ? "										
					+ "WHERE "
					+ "account_type = ? and "
					+ "account_number = ?",
					new Object[] 
					{ 	
						//accountCreation.getStrIslinkedWithCard(),
						accountCreation.getStrIsLinkedwithCard(),
						accountCreation.getStrAccountType(), 
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateAccountAfterLinkedCard()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}
	
	@Override
	public int updateLastLoginDate(AccountCreation accountCreation) 
	{
		
		String statementQuery="UPDATE pre_account_master SET last_login_date = ? WHERE mobile_no = ?";
		try 
		{
			int count = this.jdbcTemplate.update(statementQuery,
					new Object[] { 	 new Timestamp(System.currentTimeMillis()), accountCreation.getStrMobileNo() });
			amsLogger.writeInfoLog("ConfigurationDaoImpl.updateAccountCreation()" + accountCreation);
			return count;
			
		} catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public boolean isAccountAlreadyExist(AccountCreation accountCreation) 
	{
		try
		{
			String sql = "SELECT count(*) FROM account_master WHERE account_type = ? and account_number = ?";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, 
					new Object[] 
					{ 
						accountCreation.getStrAccountType(), 
						accountCreation.getStrAccountNumber() 
					});
			return count > 0;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	@Override
	public List<ams.cms.scheduler.model.AccountWiseInterestMaster> getCreditAccountWiseInterestList(ams.cms.scheduler.model.AccountWiseInterestMaster accountWiseInterestMaster) 
	{
		try {
			String statementQuery = "SELECT mcc_code AS strMcc,transaction_amount AS strTransactionAmount,"
					+"calculated_interest AS strCalculatInterest,interest_calculated_date AS strInterestCalculateDate " 
					+"FROM interest_account_wise WHERE account_type = '"+accountWiseInterestMaster.getStrAccountType()+"' "  
					+"AND account_number = '"+accountWiseInterestMaster.getStrAccountNumber()+"' " ;

			List<ams.cms.scheduler.model.AccountWiseInterestMaster> carditcardInterestWise = jdbcTemplate.query(statementQuery,
					new BeanPropertyRowMapper<ams.cms.scheduler.model.AccountWiseInterestMaster>(ams.cms.scheduler.model.AccountWiseInterestMaster.class),
					new Object[] { 
							   //accountWiseInterestMaster.getStrAccountNumber(),
							   //accountWiseInterestMaster.getStrAccountType()
							
			                 });
			return carditcardInterestWise;
		} catch (Exception e) {
			amsLogger.writeInfoLog("Exception in AccountTransactionlist::" + e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean isCustomerAccountAlreadyConfigured(AccountCreation accountCreation) 
	{
		try 
		{
			String sql = "SELECT account_number AS strAccountNumber from account_master where cust_id = '"+accountCreation.getStrCustId()+"'"
				+ "AND account_type = '"+accountCreation.getStrAccountType()+"'";
			
			List<String> strLst = this.jdbcTemplate.query(sql, new RowMapper<String>() 
			{
			    public String mapRow(ResultSet rs, int rowNum) throws SQLException
			    {
			        return rs.getString(1);
			    }
			});
			
			if (strLst.size() > 0) 
			{ 
			    return true;
			}
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
	@Override
	public AccountMaster getAccountDetails(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, am.status AS strStatus, am.account_number AS strAccountNumber, atym.account_type_category AS strAccountTypeCategory, am.first_name AS strFirstName, ");
			queryBuilder.append("email AS strEmailID, am.middle_name AS strMiddleName, am.last_name AS strLastName, atym.allow_load_cash AS strAllowLoadCash, am.load_count AS strLoadCount, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount, am.account_type AS strAccountType,");
			queryBuilder.append("am.closing_balance AS strClosingBalance, atym.category_type AS strAccountCategory, atym.gl_account_type AS strGLAccountType from account_master am INNER JOIN account_type_master atym ");
			queryBuilder.append("ON am.account_type = atym.account_type ");
			queryBuilder.append("WHERE am.account_number like '%"+accountMaster.getStrAccountNumber()+"%'");
			
			if (accountMaster.getStrAccountType()!=null && accountMaster.getStrAccountType().trim().length() > 0)
			{
				queryBuilder.append("  and am.account_type = '"+accountMaster.getStrAccountType()+"'");
			}
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateAccountMasterFields(AccountMaster accountMaster) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE account_master SET ");
			
			if (accountMaster.getStrClosingBalance() != null && accountMaster.getStrClosingBalance().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("closing_balance = ").append("?");
				objectList.add(accountMaster.getStrClosingBalance());
			}
			if (accountMaster.getStrAvailableCreditLimit() != null && accountMaster.getStrAvailableCreditLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_credit_limit = ").append("?");
				objectList.add(accountMaster.getStrAvailableCreditLimit());
			}
			if (accountMaster.getStrTotalOutstandingBal() != null && accountMaster.getStrTotalOutstandingBal().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("total_outstanding_balance = ").append("?");
				objectList.add(accountMaster.getStrTotalOutstandingBal());
			}
			if (accountMaster.getStrAvailableGracePeriod() != null && accountMaster.getStrAvailableGracePeriod().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("total_available_grace_period = ").append("?");
				objectList.add(accountMaster.getStrAvailableGracePeriod());
			}
			if(accountMaster.getPayementDueDate()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("payement_due_date = ").append("?");
				objectList.add(accountMaster.getPayementDueDate());
			}
			if (accountMaster.getGracePeriodStartDate() != null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("grace_period_start_date = ").append("?");
				objectList.add(accountMaster.getGracePeriodStartDate());
			}
			
			if (accountMaster.getStrAvailableDailyLimit()!=null && accountMaster.getStrAvailableDailyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_daily_limit = ").append("?");
				objectList.add(accountMaster.getStrAvailableDailyLimit());
			}
			if (accountMaster.getStrAvailableMonthlyLimit()!=null && accountMaster.getStrAvailableMonthlyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_monthly_limit = ").append("?");
				objectList.add(accountMaster.getStrAvailableMonthlyLimit());
			}
			if (accountMaster.getStrAvailableYearlyLimit()!=null && accountMaster.getStrAvailableYearlyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_yearly_limit = ").append("?");
				objectList.add(accountMaster.getStrAvailableYearlyLimit());
			}
			
			queryBuilder.append(" WHERE account_number = ").append("?");
			
			//queryBuilder.append(" AND  cust_id = ").append("?");
			
			objectList.add(accountMaster.getStrAccountNumber());
			
			//objectList.add(accountMaster.getStrCustId());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}

	@Override
	public AccountCreation viewAccountMasterDetails(AccountCreation accountCreation) 
	{
		try 
		{
		   StringBuilder statementQuery = new StringBuilder("SELECT am.status as strStatus, atm.status AS strAccounTypeStatus, atm.account_type_category AS accountCategoryType,");
		   statementQuery.append("closing_balance as strClosingBalance, credit_limit_amount as strCreditLimitAmount, ");
		   statementQuery.append("atm.is_revolving_credit AS strRevolvingCredit,am.account_number As strAccountNumber,am.account_type AS strAccountType, ");
		   statementQuery.append("credit_limit_category as strCreditLimitCategory, available_credit_limit as strAvailableCreditLimit, ");
		   statementQuery.append("available_daily_limit as strAvailableDailyLimit, available_monthly_limit as strAvailableMonthlyLimit, ");
		   statementQuery.append("available_yearly_limit as strAvailableYearlyLimit, total_outstanding_balance as strTotalOutstandingBal ");
		   statementQuery.append("from account_master am inner JOIN account_type_master atm ON am.account_type = atm.account_type ");
		   statementQuery.append("where account_number = ? ");
		   
		   
			List<AccountCreation> accountTxnlist  = jdbcTemplate.query(statementQuery.toString(),
					new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[] 
			{ 
				accountCreation.getStrAccountNumber()
			});
			if(accountTxnlist != null && accountTxnlist.size() > 0)
			{
				return accountTxnlist.get(0);
			}
			
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in AccountTransactionlist::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateBalnceLimitValues(AccountCreation accountCreation) {
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "closing_balance = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrClosingBalance(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}

	@Override
	public AccountCreation getSenderAccountInformation(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT am.id AS strID, am.account_number AS strAccountNumber, am.status as strStatus, atm.status AS strAccounTypeStatus, ");
			queryBuilder.append("am.account_type AS strAccountType, am.closing_balance AS strClosingBalance, email AS strEmailID, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("atm.account_type_category AS accountCategoryType, atm.is_revolving_credit AS strIsRevolvingCredit, ");
			queryBuilder.append("rccm.grace_period_in_days AS revolvingGracePeriodInDays, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,");
			queryBuilder.append("am.available_credit_limit AS strAvailableCreditLimit, am.available_daily_limit AS strAvailableDailyLimit, ");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit, am.available_yearly_limit AS strAvailableYearlyLimit, ");
			queryBuilder.append("am.total_outstanding_balance AS strTotalOutstandingBal, rccm.billing_cycle_date AS strBillingCycleDate, ");
			
			queryBuilder.append("glatm.account_type AS strGLAccountType, glatm.account_number AS strGLAccountNo, ");
			queryBuilder.append("glatm.account_description AS strGLAccountDescription, glatm.closing_balance AS strGLAccountBalance ");
			
			queryBuilder.append("FROM account_master AS am ");
			queryBuilder.append("INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			queryBuilder.append("LEFT JOIN revolving_credit_card_master rccm ON rccm.account_type = am.account_type ");
			
			queryBuilder.append("LEFT JOIN gl_account_type_master glatm ON glatm.account_type = atm.gl_account_type ");
			
			queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber()+"' AND am.cust_id = '"+accountCreation.getStrCustId()+"'");
			
			amsLogger.writeInfoLog("getSenderAccountInformation:: query=["+queryBuilder.toString()+"]");
			
			List<AccountCreation> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Override
	public AccountCreation getRecipientAccountInformation(AccountCreation accountCreation) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (accountCreation.getStrParticipantID()!=null && accountCreation.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountCreation.getStrParticipantID()));
			}
			if (accountCreation.getStrAccountType()!=null && accountCreation.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountCreation.getStrAccountType()));
			}
			if (accountCreation.getStrAccountNumber()!=null && accountCreation.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", accountCreation.getStrAccountNumber()));
			}
			if (accountCreation.getStrMobileNo()!=null && accountCreation.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", accountCreation.getStrMobileNo()));
			}
			if (accountCreation.getStrCustId()!=null && accountCreation.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", accountCreation.getStrCustId()));
			}
			
			List<AccountCreation> listData = (List<AccountCreation>) criteria.list();
			if (listData !=null && listData.size() > 0) 
			{
				return listData.get(0);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception in getAccountInformation::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	*/
	
	public AccountCreation getRecipientAccountInformation(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT am.id AS strID, am.account_number AS strAccountNumber, am.status as strStatus, atm.status AS strAccounTypeStatus, ");
			queryBuilder.append("am.cust_id AS strCustId, am.account_type AS strAccountType, am.closing_balance AS strClosingBalance, ");
			queryBuilder.append("atm.account_type_category AS accountCategoryType, atm.is_revolving_credit AS strIsRevolvingCredit, ");
			queryBuilder.append("rccm.grace_period_in_days AS revolvingGracePeriodInDays, email AS strEmailID, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.available_credit_limit AS strAvailableCreditLimit, am.available_daily_limit AS strAvailableDailyLimit, ");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit, am.available_yearly_limit AS strAvailableYearlyLimit, ");
			queryBuilder.append("am.total_outstanding_balance AS strTotalOutstandingBal, rccm.billing_cycle_date AS strBillingCycleDate, ");
			queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, atm.gl_account_type AS strGLAccountType ");
			queryBuilder.append("FROM account_master AS am ");
			queryBuilder.append("INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			queryBuilder.append("LEFT JOIN revolving_credit_card_master rccm ON rccm.account_type = am.account_type ");
			queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber()+"' AND am.cust_id = (SELECT cust_id FROM account_master sbam WHERE sbam.account_number = '"+accountCreation.getStrAccountNumber()+"')");
			
			amsLogger.writeInfoLog("getSenderAccountInformationWithoutCustId:: query=["+queryBuilder.toString()+"]");
			
			List<AccountCreation> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	

	//change the query for account profile --link image view  
	@Override
	public List<AccountCreation> getSignInDataFromApp(AccountCreation accountCreation)
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,");
			selectQuerySb.append("am.cust_id AS strCustId, am.mobile_no AS strMobileNo,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			selectQuerySb.append("am.account_number AS strAccountNumber, am.account_type AS strAccountType,");
			selectQuerySb.append("am.qr_code_image_url AS qrCodeImageUrl, atm.account_type_category AS accountCategoryType, atm.description AS description , atm.is_multi_currency_support AS isMultiCurrencySupport ,");
			selectQuerySb.append("am.closing_balance AS availableBalance, am.qr_code_file_path AS qrCodeImagePath, ");
			selectQuerySb.append("kyc.tier1_passport_photograph AS strTier1PassportPhotograph ");
			selectQuerySb.append("FROM account_master am ");
			selectQuerySb.append("INNER JOIN account_type_master atm ");
			selectQuerySb.append("ON am.account_type = atm.account_type ");
			selectQuerySb.append("INNER JOIN kyc_details kyc ");
			selectQuerySb.append("ON kyc.mobile_no = am.mobile_no ");
			selectQuerySb.append("WHERE ");
			selectQuerySb.append("am.mobile_no = '"+accountCreation.getStrMobileNo()+"' AND am.cust_id = '"+accountCreation.getStrCustId()+"' ");
			
/*			StringBuilder selectQuerySb = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,");
			selectQuerySb.append("am.cust_id AS strCustId, am.mobile_no AS strMobileNo,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
	    	selectQuerySb.append("am.account_number AS strAccountNumber, am.account_type AS strAccountType,");
		    selectQuerySb.append("am.qr_code_image_url AS qrCodeImageUrl, atm.account_type_category AS accountCategoryType, atm.description AS description , atm.is_multi_currency_support AS isMultiCurrencySupport ,");
			selectQuerySb.append("am.closing_balance AS availableBalance, am.qr_code_file_path AS qrCodeImagePath,");
			selectQuerySb.append("kyc.tier1_passport_photograph AS tier1PassportPhoto ");
			selectQuerySb.append(" FROM account_master am INNER JOIN account_type_master atm ");
			selectQuerySb.append(" ON am.account_type = atm.account_type ");
			selectQuerySb.append(" INNER JOIN kyc_details kyc ON kyc.mobile_no = am.mobile_no ");
			selectQuerySb.append(" WHERE am.mobile_no = ?");
			selectQuerySb.append(" AND am.cust_id = ?");
*/			
			List<AccountCreation> signInDatalist = jdbcTemplate.query(selectQuerySb.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[] {});
			
			return signInDatalist;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//change the query -add- by sagar
	
	
	@Override
	public List<AccountCreation> getUpdatedAccountsListForApp(AccountCreation accountCreation)
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			selectQuerySb.append("am.cust_id AS strCustId, am.mobile_no AS strMobileNo,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			selectQuerySb.append("am.account_number AS strAccountNumber,  am.account_type  AS strAccountType , atm.description AS description , ");
			selectQuerySb.append("am.qr_code_image_url AS qrCodeImageUrl, atm.account_type_category AS accountCategoryType , ");
			selectQuerySb.append("am.closing_balance AS availableBalance, am.qr_code_file_path AS qrCodeImagePath , atm.is_multi_currency_support AS isMultiCurrencySupport");
			selectQuerySb.append(" FROM account_master am INNER JOIN account_type_master atm ");
			selectQuerySb.append("ON am.account_type = atm.account_type WHERE am.cust_id = ?");
			
			List<AccountCreation> updatedAccountsList = jdbcTemplate.query(selectQuerySb.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[] 
			{
				accountCreation.getStrCustId()
			}
			);
			return updatedAccountsList;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public int updateAccountMasterFieldsBasedOnQuery(String updateQuery) 
	{
		try 
		{
			int count = this.jdbcTemplate.update(updateQuery, new Object[]{});
			amsLogger.writeInfoLog("Count of updateAccountMasterFieldsBasedOnQuery:::" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int updateCreditLimitValues(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "available_credit_limit = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrAvailableCreditLimit(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}
	
	@Override
	public int updateLimitValues(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "available_daily_limit = ?, "
					+ "available_monthly_limit = ?, "
					+ "available_yearly_limit = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrAvailableDailyLimit(),
						accountCreation.getStrAvailableMonthlyLimit(),
						accountCreation.getStrAvailableYearlyLimit(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}

	@Override
	public AccountCreation updategracePeriodDate(AccountCreation accountCreation) 
	{
		try 
		{
			amsLogger.writeInfoLog("accountCreation.getGracePeriodStartDate()"+accountCreation.getStrGracePeriodStartDate());
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "grace_period_start_date = ?, total_available_grace_period = ?,  payement_due_date = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrGracePeriodStartDate(),
						accountCreation.getStrAvailableGracePeriod(),
						accountCreation.getPayementDueDate(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("DAte::::::"+accountCreation.getStrGracePeriodStartDate());
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateTotalOutstanding(AccountCreation accountCreation) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "total_outstanding_balance = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrTotalOutstandingBal(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateTotalOutstanding()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public int updateAccountBalance(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "closing_balance = ?, load_count = ? "
					+ "WHERE account_number = ? AND account_type = ?",
					new Object[] 
					{ 	
						accountCreation.getStrClosingBalance(),
						accountCreation.getStrLoadCount(),
						accountCreation.getStrAccountNumber(),
						accountCreation.getStrAccountType()
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int updateAccountCreationFields(AccountCreation accountCreation) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE account_master SET ");
			
			if (accountCreation.getStrClosingBalance() != null && accountCreation.getStrClosingBalance().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("closing_balance = ").append("?");
				objectList.add(accountCreation.getStrClosingBalance());
			}
			if (accountCreation.getStrAvailableCreditLimit() != null && accountCreation.getStrAvailableCreditLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_credit_limit = ").append("?");
				objectList.add(accountCreation.getStrAvailableCreditLimit());
			}
			if (accountCreation.getStrTotalOutstandingBal() != null && accountCreation.getStrTotalOutstandingBal().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("total_outstanding_balance = ").append("?");
				objectList.add(accountCreation.getStrTotalOutstandingBal());
			}
			if (accountCreation.getStrAvailableGracePeriod() != null && accountCreation.getStrAvailableGracePeriod().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("total_available_grace_period = ").append("?");
				objectList.add(accountCreation.getStrAvailableGracePeriod());
			}
			if (accountCreation.getGracePeriodStartDate() != null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("grace_period_start_date = ").append("?");
				objectList.add(accountCreation.getGracePeriodStartDate());
			}			
			if (accountCreation.getStrAvailableDailyLimit()!=null && accountCreation.getStrAvailableDailyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_daily_limit = ").append("?");
				objectList.add(accountCreation.getStrAvailableDailyLimit());
			}
			if (accountCreation.getStrAvailableMonthlyLimit()!=null && accountCreation.getStrAvailableMonthlyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_monthly_limit = ").append("?");
				objectList.add(accountCreation.getStrAvailableMonthlyLimit());
			}
			if (accountCreation.getStrAvailableYearlyLimit()!=null && accountCreation.getStrAvailableYearlyLimit().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("available_yearly_limit = ").append("?");
				objectList.add(accountCreation.getStrAvailableYearlyLimit());
			}
			if (accountCreation.getStrEarMarkAmount()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("ear_mark_amount = ").append("?");
				objectList.add(accountCreation.getStrEarMarkAmount());
			}
			if (accountCreation.getStrPreCredAmount()!=null)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("pre_cred_amount = ").append("?");
				objectList.add(accountCreation.getStrPreCredAmount());
			}
			
			queryBuilder.append(" WHERE account_number = ").append("?");
			
			//queryBuilder.append(" AND  cust_id = ").append("?");
			
			objectList.add(accountCreation.getStrAccountNumber());
			
			//objectList.add(accountCreation.getStrCustId());
			
			Object[] object = objectList.toArray();
			
			amsLogger.writeInfoLog("In updateAccountCreationFields query::\n"+queryBuilder.toString());
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}

	@Override
	public AccountCreation getSenderAccountInformationWithoutCustId(AccountCreation accountCreation)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT am.id AS strID, am.account_number AS strAccountNumber, am.status as strStatus, atm.status AS strAccounTypeStatus, ");
			queryBuilder.append("am.cust_id AS strCustId, am.account_type AS strAccountType, am.closing_balance AS strClosingBalance, ");
			queryBuilder.append("atm.account_type_category AS accountCategoryType, atm.is_revolving_credit AS strIsRevolvingCredit, ");
			queryBuilder.append("rccm.grace_period_in_days AS revolvingGracePeriodInDays, email AS strEmailID, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.available_credit_limit AS strAvailableCreditLimit, am.available_daily_limit AS strAvailableDailyLimit, ");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit, am.available_yearly_limit AS strAvailableYearlyLimit, ");
			queryBuilder.append("am.total_outstanding_balance AS strTotalOutstandingBal, rccm.billing_cycle_date AS strBillingCycleDate, ");
			queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			
			queryBuilder.append("glatm.account_type AS strGLAccountType, glatm.account_number AS strGLAccountNo, ");
			queryBuilder.append("glatm.account_description AS strGLAccountDescription, glatm.closing_balance AS strGLAccountBalance ");
			
			queryBuilder.append("FROM account_master AS am ");
			queryBuilder.append("INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			queryBuilder.append("LEFT JOIN revolving_credit_card_master rccm ON rccm.account_type = am.account_type ");
			queryBuilder.append("LEFT JOIN gl_account_type_master glatm ON glatm.account_type = atm.gl_account_type ");
			
			queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber()+"' AND am.cust_id = (SELECT cust_id FROM account_master sbam WHERE sbam.account_number = '"+accountCreation.getStrAccountNumber()+"')");
			
			amsLogger.writeInfoLog("getSenderAccountInformationWithoutCustId:: query=["+queryBuilder.toString()+"]");
			
			List<AccountCreation> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public AccountCreation getRecipientAccountInformationWithoutCustId(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT am.id AS strID, am.account_number AS strAccountNumber, am.status as strStatus, atm.status AS strAccounTypeStatus, ");
			queryBuilder.append("am.cust_id AS strCustId, am.account_type AS strAccountType, am.closing_balance AS strClosingBalance, ");
			queryBuilder.append("atm.account_type_category AS accountCategoryType, atm.is_revolving_credit AS strIsRevolvingCredit, ");
			queryBuilder.append("rccm.grace_period_in_days AS revolvingGracePeriodInDays, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.available_credit_limit AS strAvailableCreditLimit, am.available_daily_limit AS strAvailableDailyLimit, ");
			queryBuilder.append("am.available_monthly_limit AS strAvailableMonthlyLimit, am.available_yearly_limit AS strAvailableYearlyLimit, ");
			queryBuilder.append("am.total_outstanding_balance AS strTotalOutstandingBal, rccm.billing_cycle_date AS strBillingCycleDate, ");
			queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			
			queryBuilder.append("glatm.account_type AS strGLAccountType, glatm.account_number AS strGLAccountNo,");
			queryBuilder.append("glatm.account_description AS strGLAccountDescription, glatm.closing_balance AS strGLAccountBalance ");
			
			queryBuilder.append("FROM account_master AS am ");
			queryBuilder.append("INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			queryBuilder.append("LEFT JOIN revolving_credit_card_master rccm ON rccm.account_type = am.account_type ");
			queryBuilder.append("LEFT JOIN gl_account_type_master glatm ON glatm.account_type = atm.gl_account_type ");
			
			queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber()+"' AND am.cust_id = (SELECT cust_id FROM account_master sbam WHERE sbam.account_number = '"+accountCreation.getStrAccountNumber()+"')");
			
			amsLogger.writeInfoLog("getSenderAccountInformationWithoutCustId:: query=["+queryBuilder.toString()+"]");
			
			List<AccountCreation> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	//created by ankit
	@Override
	public List<AccountCreation> getAcountNameAndBalance(AccountCreation accountCreation) 
	{
		try 
		{
			 /*String sql = "select CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
					+ " closing_balance as strClosingBalance, cust_id as strCustId"
					+ " FROM account_master"
					+ " WHERE account_type = ? AND account_number = ?";
				*/
			 
			 
			 StringBuilder querysb = new StringBuilder("select CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,");
			 querysb.append("am.closing_balance as strClosingBalance, am.cust_id as strCustId, atm.account_type_category AS accountCategoryType,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			 querysb.append("DATE_FORMAT(am.payement_due_date, '%d-%M-%Y') AS strPaymentDueDate, am.credit_limit_amount AS strCreditLimitAmount,");
			 querysb.append("am.available_credit_limit AS strAvailableCreditLimit, am.total_outstanding_balance AS strTotalOutstandingBal,");
			 querysb.append("REPLACE(DATE_FORMAT(DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL + 1 MONTH ), \"%d-%M-%Y\"), ");
			 querysb.append("SUBSTRING(DATE_FORMAT(DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL + 1 MONTH ), \"%d-%M-%Y\"), 1, ");
			 querysb.append("POSITION('-' IN DATE_FORMAT(DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL + 1 MONTH ), \"%d-%M-%Y\")) -1 ), rrcm.billing_cycle_date) AS strBillingCycleDate");
			 querysb.append(" FROM account_master am ");
			 querysb.append(" LEFT JOIN account_type_master atm ON am.account_type = atm.account_type ");
			 querysb.append(" LEFT JOIN revolving_credit_card_master rrcm ON rrcm.account_type = atm.account_type ");
			 querysb.append(" WHERE am.account_type = ? ");
			 querysb.append(" AND am.account_number = ?");
					
			 List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(querysb.toString(), 
						(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			 new Object[] 
					{ 	
						accountCreation.getStrAccountType(),
						accountCreation.getStrAccountNumber() 
					});
			
			return accouontCreationList;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}
	//created by ankit
	@Override
	public AccountMaster getAccountQRDetails(AccountMaster accountMaster ) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT qr_code_data as strQrCodeData, qr_code_file_path as strQrCodeFilePath, ");
			queryBuilder.append("qr_code_image_url as qrCodeImageUrl, account_type as strAccountType from account_master ");
			queryBuilder.append("WHERE account_number = '"+accountMaster.getStrAccountNumber()+"'");
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			     new Object[]  {});
			
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateTransactionLimit(AccountCreation accountCreation)
	{
		try
		{
			StringBuilder updateQuerySb = new StringBuilder("UPDATE account_master SET ");
			updateQuerySb.append("per_txn_limit = ? , daily_txn_limit = ? , ");
			updateQuerySb.append("monthly_txn_limit = ? ");
			updateQuerySb.append("WHERE account_number = ? AND cust_id = ? ");
					
			int count = this.jdbcTemplate.update(updateQuerySb.toString(),
					new Object[] 
					{
						accountCreation.getStrSingleTxnLimit(),
						accountCreation.getStrDailyTxnLimit(),
						accountCreation.getStrMonthlyTxnLimit(),
						accountCreation.getStrAccountNumber(),
						accountCreation.getStrCustId()
					});
			amsLogger.writeInfoLog("Count updateTransactionLimit()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public AccountCreation getTransactionLimits(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT atl.single_txn_limit AS strSingleTxnLimit, ");
			selectQuerySb.append("atl.daily_txn_limit AS strDailyTxnLimit , atl.monthly_txn_limit AS strMonthlyTxnLimit , ");
			selectQuerySb.append("am.account_type AS strAccountType , am.account_number AS strAccountNumber ");
			selectQuerySb.append("FROM account_master am INNER JOIN account_transaction_limit atl ON atl.account_type = am.account_type ");
			selectQuerySb.append("WHERE account_number = ? AND cust_id = ?");
			
			List<AccountCreation> existingTxnLimits = this.jdbcTemplate.query(selectQuerySb.toString(),
					(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] { accountCreation.getStrAccountNumber(), accountCreation.getStrCustId()}
			);
			
			if(existingTxnLimits!=null && existingTxnLimits.size() >0)
			{
					accountCreation = existingTxnLimits.get(0);
			}
			return accountCreation;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public AccountMaster getAccountNameByAccountNo(AccountMaster accountMaster ) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID, am.cust_id AS strCustId,	am.mobile_no AS strMobileNo, am.status AS strStatus, ");
			queryBuilder.append("atm.account_type_category AS strAccountTypeCategory, am.account_type AS strAccountType, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			queryBuilder.append("atm.is_withdraw_allow_at_agent AS strIsWithdrawAllowAtAgent, atm.is_deposit_allow_at_agent AS strIsDepositAllowAtAgent, ");
			queryBuilder.append("am.closing_balance AS strClosingBalance from account_master am ");
			queryBuilder.append("inner join account_type_master atm on atm.account_type = am.account_type ");
			queryBuilder.append("WHERE am.account_number = ? ");
			//AND am.account_type = ?
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			new Object[] 
			{
			    		 accountMaster.getStrAccountNumber()
			    		// accountMaster.getStrAccountType()
			});
			if (accountMasters != null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	// created by ankit on 15-04-2023
	@Override
	public List<AccountCreation> isMerchantVerified(AccountCreation accountCreation) 
	{
		try 
		{
			String sql = "SELECT CONCAT_WS('', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,"
					+ " am.cust_id as strCustId, am.account_type as strAccountType, am.status as strStatus,"
					+ " am.account_number as strAccountNumber"
					+ " FROM account_master am"
					+ " WHERE cust_id = ?"
					+ " AND account_type = ? AND account_number = ?";
			
			 List<AccountCreation> accouontCreationList = 
			 this.jdbcTemplate.query(sql,(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class), 
			 new Object[]
			 {
				accountCreation.getStrCustId(), 
				accountCreation.getStrAccountType(),
				accountCreation.getStrAccountNumber() 
			 });
			 
			return accouontCreationList;

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	// created by ankit on 15-04-2023

	// created by ankit on 16-04-2023
	/*
	 * fetching based on AccountNumber
	 */
	@Override
	public List<AccountCreation> getTransactionPayeeDetailsByAccountNumber(AccountCreation accountCreation) 
	{
		try 
		{
			String sql = "select CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,"
					+ " am.status as strStatus,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,"
					+ " am.account_number as strAccountNumber,"
					+ " am.closing_balance as strClosingBalance, am.cust_id as strCustId,"
					+ " am.account_type as strAccountType,"
					+ " am.ear_mark_amount as strEarMarkAmount,"
					+ " am.pre_cred_amount as strPreCredAmount"
					+ " FROM account_master am"
					+ " WHERE am.account_number = ?";

			List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql,
					(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] { accountCreation.getStrAccountNumber() });

			return accouontCreationList;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	// created by ankit on 16-04-2023
	// fetching merchant details based on cust_id will need to change on account
	// Type and Account_

	@Override
	public List<AccountCreation> getAccountInfoAndStatus(AccountCreation accountCreation)
	{
		try 
		{
			String sql = "SELECT cust_id as strCustId, account_type as strAccountType,"
					+ " account_number as strAccountNumber, first_name as strFirstName,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,"
					+ " middle_name as strMiddleName, last_name as StrLastName,"
					+ "CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
//					+ " gl_account_type as strGLAccountType,gl_account_no as strGLAccountNumber,"
					+ " status as strStatus, closing_balance as strClosingBalance" 
					+ " FROM account_master"
			        + " WHERE account_type = ? AND account_number = ?";

			List<AccountCreation> accountCreationList = this.jdbcTemplate.query(sql,
					(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] {
								accountCreation.getStrAccountType(),
								accountCreation.getStrAccountNumber()
							});

			return accountCreationList;

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	// created by ankit on 16-04-2023
	@Override
	public int updateOnlyBalance(AccountCreation accountCreation)
	{
		String sqlUpdateQuery = "UPDATE account_master" 
							   +" SET"
							   +" closing_balance = ?"
							   +" WHERE account_type = ? AND account_number = ?";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] { accountCreation.getStrClosingBalance(),
					accountCreation.getStrAccountType(), accountCreation.getStrAccountNumber() });
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	// created by ankit on 16-04-2023

	// created by ankit on 17-04-2023
	@Override
	public List<AccountCreation> getReceiverInformation(AccountCreation accountCreation)
	{
		try
		{
			String sql = "select first_name as strFirstName, status as strStatus,"
					+ " middle_name as strMiddleName , last_name as strLastName,"
					+ " closing_balance as strClosingBalance, cust_id as strCustId" + " account_type as strAccountType"
					+ " FROM account_master" + " WHERE account_type = ? AND account_number = ?";

			List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql,
					(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] { accountCreation.getStrAccountType(), accountCreation.getStrAccountNumber() });

			return accouontCreationList;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	// created by ankit on 17-04-2023
	
	//Added by Pankaj Pawar Start
	@Override
	public AccountMaster getAccountInfo(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT id as strID,email as strEmailID, status as strStatus, cust_id as strCustId, ");
			queryBuilder.append("closing_balance as strClosingBalance, available_daily_limit as strAvailableDailyLimit, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("available_monthly_limit as strAvailableMonthlyLimit, available_yearly_limit as strAvailableYearlyLimit from account_master am");
			queryBuilder.append("WHERE account_number = ? AND account_type = ?");
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			     new Object[]  {
			    		 accountMaster.getStrAccountNumber(),
			    		 accountMaster.getStrAccountType()
			     });
			
			if (accountMasters != null && accountMasters.size() > 0) 
			{
				amsLogger.writeInfoLog("Account MAster::::"+accountMasters.get(0));
				return accountMasters.get(0);
			}	
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public int updatePerformTxnValues(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "available_daily_limit = ?, "
					+ "available_monthly_limit = ?, "
					+ "available_yearly_limit = ?, "
					+ "closing_balance = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrAvailableDailyLimit(),
						accountCreation.getStrAvailableMonthlyLimit(),
						accountCreation.getStrAvailableYearlyLimit(),
						accountCreation.getStrClosingBalance(),
						accountCreation.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}
	
	@Override
	public List<AccountMaster> getAccountInfoList(UserTransactionModel userTransactionModel)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id as strID, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName,");
			queryBuilder.append("am.account_number AS strAccountNumber, am.account_type AS strAccountType, am.email as strEmailID,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.status as strStatus, am.cust_id as strCustId, ");
			queryBuilder.append("am.closing_balance as strClosingBalance, am.available_daily_limit as strAvailableDailyLimit, ");
			queryBuilder.append("am.available_monthly_limit as strAvailableMonthlyLimit, am.available_yearly_limit as strAvailableYearlyLimit, ");
			queryBuilder.append("atm.gl_account_type AS strGLAccountType, atm.gl_account_no AS strGLAccountNo ");
			queryBuilder.append("from account_master am INNER JOIN account_type_master atm ON atm.account_type = am.account_type ");
			queryBuilder.append("WHERE account_number IN('"+userTransactionModel.getFromAccountNo()+"','"+userTransactionModel.getToAccountNumber()+"')");
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			     new Object[]  {
			    		 //userTransactionModel.getFromAccountNo(),
			    		 //userTransactionModel.getToAccountNumber()
			     });
			
			if (accountMasters != null && accountMasters.size() > 0) 
			{
				return accountMasters;
			}	
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//Added by Pankaj Pawar End
	
	//Added by prashant
	@Override
	public AccountCreation getClosingBalanceonAccTypeAccNo(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.account_type AS strAccountType, am.account_number AS strAccountNumber, am.closing_balance AS strClosingBalance, atc.charge_type AS strChargeType, atc.amount AS strAmount, ");
			queryBuilder.append("am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount From account_master am ");
			queryBuilder.append("INNER JOIN  account_type_charges atc ");
			queryBuilder.append("ON atc.account_type = am.account_type ");
			queryBuilder.append("WHERE am.account_type = ? AND am.account_number = ? AND atc.charge_type = ? ");
			
			List<AccountCreation> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			     new Object[]  { accountCreation.getStrAccountType(), accountCreation.getStrAccountNumber(), accountCreation.getStrChargeType()});
			if (accountCretion!=null && accountCretion.size() > 0) 
			{
				return accountCretion.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateClosingBalance(AccountCreation accountCreation) 
	{
	  try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "closing_balance = ? "
					+ "WHERE account_number = ? AND account_type = ?",
					new Object[] 
					{ 	
						accountCreation.getStrClosingBalance(),
						accountCreation.getStrAccountNumber(),
						accountCreation.getStrAccountType()
					});
			amsLogger.writeInfoLog("AccountMasterDaoImpl.updateClosingBalance()"+count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
		 
	@Override
	public List<AccountCreation> getRegCustWithLinkAccount(AccountCreation accountCreation) 
	{
		{
			String regCustWithLinkedAcc = ""
					+" SELECT am.cust_id AS strCustId,creation_date AS strDateOfRegistration , pam.fname AS strFirstName ,pam.mname AS strMiddleName,am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,"
					+" pam.lname AS strLastName,am.account_type AS strAccountType , am.account_number AS strAccountNumber, pam.account_issue_date AS strAccountIssueDate "
					+" FROM pre_account_master pam "
					+" INNER JOIN account_master am "
					+" WHERE account_issue_date BETWEEN ? AND ? AND am.status = ? ";
			
			List<AccountCreation> getRegCustWithLinkAccount = jdbcTemplate.query(regCustWithLinkedAcc,
					new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] { accountCreation.getFromDate(), 
							accountCreation.getToDate(), 
							accountCreation.getStrStatus()}
					);

			return getRegCustWithLinkAccount;
		}
	}
	//Added by Pankaj [start]
	@Override
	public AccountCreation getTierInfoByCustID(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT active_tier as strActiveTier, tier1_status as strTier1, tier2_status as strTier2, ");
			queryBuilder.append("tier3_status as strTier3 FROM customer_master WHERE cust_id = ? ");
			
			List<AccountCreation> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			     new Object[]  { accountCreation.getStrAccountType(), accountCreation.getStrAccountNumber(), accountCreation.getStrChargeType()});
			if (accountCretion!=null && accountCretion.size() > 0) 
			{
				return accountCretion.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//Added by Pankaj [End]

	@Override
	public List<AccountMasterResponse> getAccountInformationForClosingAccount(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			queryBuilder.append("am.closing_balance AS strAvailableBalance, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.account_type AS strAccountType, am.account_number AS strAccountNo, am.`status` AS strAccountStatus, ");
			queryBuilder.append("cm.active_tier AS strCurrentrTier, kyc.address_proof_document_type AS strPOADocumentType, ");
			queryBuilder.append("kyc.address_proof_document_name AS strPOADocumentName, kyc.address_proof_document_value AS strPOADocumentValue,");
			queryBuilder.append("kyc.identity_proof_document_type AS strPOIDocumentType, kyc.identity_proof_document_name AS strPOIDocumentName,");
			queryBuilder.append("kyc.identity_proof_document_value AS strPOIDocumentValue, kyc.tier1_passport_photograph AS strTier1PassportPhotograph,");
			queryBuilder.append("kyc.tier2_passport_photograph AS strTier2PassportPhotograph, kyc.bvn_no AS strBvnNo ");
			queryBuilder.append("FROM account_master am LEFT JOIN customer_master cm ON am.cust_id = cm.cust_id ");
			queryBuilder.append("LEFT JOIN kyc_details kyc ON am.mobile_no = kyc.mobile_no ");
			queryBuilder.append("WHERE am.cust_id = ? AND am.`status` = 'Active'");
			
			List<AccountMasterResponse> accountMasterResponses  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMasterResponse>(AccountMasterResponse.class),
			     new Object[] { accountCreation.getStrCustId()});
			if (accountMasterResponses!=null && accountMasterResponses.size() > 0) 
			{
				return accountMasterResponses;
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//added  by sunil Y[start]
	@Override
	public int updateAccountStatus(AccountMaster accountMaster)
	{
		String sqlUpdateQuery = "UPDATE account_master SET status = ?, pre_closure_date = ? WHERE account_number = ? ";
		try {
			
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] { accountMaster.getStrStatus(),
					accountMaster.getStrPreClosureDate(), accountMaster.getStrAccountNumber() });
			amsLogger.writeInfoLog(sqlUpdateQuery);
			return count;

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		amsLogger.writeInfoLog(e.getMessage().toString());	
		}
		return 0;

	}

	//added by Sunil Y,
	@Override
	public AccountMaster getAccountMasterByAccountNumber(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT account_number as strAccountNumber, status as strStatus, account_type as strAccountType ,closing_balance AS strClosingBalance ,");
			queryBuilder.append("pre_closure_date as strPreClosureDate , phone_no as strPhoneNo  from account_master ");
			queryBuilder.append("WHERE account_number = '" + accountMaster.getStrAccountNumber() + "'");

			List<AccountMaster> accountMasters = jdbcTemplate.query(queryBuilder.toString(),
					new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class), new Object[] {});
			amsLogger.writeInfoLog(queryBuilder.toString());
			if (accountMasters != null && accountMasters.size() > 0) {
				return accountMasters.get(0);
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateAccountStatusAndBlance(AccountMaster accountMaster) 
	{
		String sqlUpdateQuery = "UPDATE account_master SET status = ?, closing_balance = ? WHERE account_number = ? ";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] { accountMaster.getStrStatus(),
					accountMaster.getStrClosingBalance(), accountMaster.getStrAccountNumber() });
			return count;

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	//added by Sunil Y , [End]
	
	@Override
	public List<AccountCreation> getAccountHolderName(AccountCreation accountCreation) 
	{
		try 
		{
			 String sql = "select CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
					+ " FROM account_master"
					+ " WHERE account_type = ? AND account_number = ?";
					
			 List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql, 
						(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			 new Object[] 
					{ 	
						accountCreation.getStrAccountType(),
						accountCreation.getStrAccountNumber() 
					});
			
			return accouontCreationList;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}

	//created by ankit on 06-06-2023
	@Override
	public List<AccountCreation> findByAccountNumber(AccountCreation accountCreation)
	{
		try 
		{
			String sql = "select first_name as strFirstName, status as strStatus,"
					+ " middle_name as strMiddleName , last_name as strLastName,"
					+ " CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
					+ " closing_balance as strClosingBalance, cust_id as strCustId,"
					+ " ear_mark_amount as strEarMarkAmount,"
					+ " pre_cred_amount as strPreCredAmount,"
					+ " account_type as strAccountType,"
					+ " account_number as strAccountNumber"
					+ " FROM account_master"
					+ " WHERE account_number = ?";			
			 List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql, 
						(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			 new Object[] 
					{ 	
						accountCreation.getStrAccountNumber() 
					});
			
			return accouontCreationList;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;
	}
	//created by ankit on 06-06-2023

	@Override
	public void updateEarMark(AccountCreation accountCreation) 
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "ear_mark_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try {
			this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountCreation.getStrEarMarkAmount(),
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			/*return count;*/			
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
//		return 0;
}

	@Override
	public void reduceEarMarkAmount(AccountCreation accountCreation)
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "closing_balance = ?,"
				+ "ear_mark_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try {
			int update = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountCreation.getStrClosingBalance(),
					accountCreation.getStrEarMarkAmount(),
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			amsLogger.writeInfoLog("Updated the values to "+update +"earMark "+accountCreation.getStrEarMarkAmount()+
					"clsoing balance to "+ accountCreation.getStrClosingBalance());
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	
	@Override
	public AccountCreation getEarMarkAndClosingBalance(AccountCreation accountCreation) 
	{	
		try 
		{
			String sql = "select first_name as strFirstName, status as strStatus,"
					+ " middle_name as strMiddleName , last_name as strLastName,"
					+ " CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
					+ " closing_balance as strClosingBalance, cust_id as strCustId,"
					+ " ear_mark_amount as strEarMarkAmount,"
					+ " account_type as strAccountType"
					+ " FROM account_master"
					+ " WHERE account_number = ?";
					
			 List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql, 
						(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			 new Object[] 
					{ 	
						accountCreation.getStrAccountNumber() 
					});
			
			return accouontCreationList.get(0);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	public void preCredAccount(AccountCreation accountMaster) 
	{	
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "pre_cred_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try {
			int update = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountMaster.getStrPreCredAmount(),
					accountMaster.getStrAccountType(), 
					accountMaster.getStrAccountNumber() 
				}
			);
			amsLogger.writeInfoLog(update);
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public AccountCreation getPreCredAndClosingBalance(AccountCreation accountCreation) 
	{	
		try 
		{
			String sql = "select first_name as strFirstName, status as strStatus,"
					+ " middle_name as strMiddleName , last_name as strLastName,"
					+ " CONCAT_WS(' ', first_name, middle_name, last_name) AS strAccountHolderName,"
					+ " closing_balance as strClosingBalance, cust_id as strCustId,"
					+ " pre_cred_amount as strPreCredAmount,"
					+ " account_type as strAccountType"
					+ " FROM account_master"
					+ " WHERE account_number = ?";
					
			 List<AccountCreation> accouontCreationList = this.jdbcTemplate.query(sql, 
						(RowMapper<AccountCreation>) new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
			 new Object[] 
					{ 	
						accountCreation.getStrAccountNumber() 
					});
			
			return accouontCreationList.get(0);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}

		return null;
	}
	
	
	@Override
	public int earMarkTransactionAmount(AccountCreation accountCreation)
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "ear_mark_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountCreation.getStrEarMarkAmount(),
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			return count;			
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public int preCredTransactionAmount(AccountCreation accountCreation) 
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "pre_cred_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try {
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountCreation.getStrPreCredAmount(),
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			System.out.print("pre Cre Upadte "+ count);
			return count;
			
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@Override
	public void addPreCredAmount(AccountCreation accountCreation) 
	{
		String sqlUpdateQuery = "UPDATE account_master "
				+ "SET "
				+ "closing_balance = ?, "
				+ "pre_cred_amount = ? "
				+ "where account_type = ? AND account_number = ?";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[]
				{
					accountCreation.getStrClosingBalance(),
					accountCreation.getStrPreCredAmount(),
					accountCreation.getStrAccountType(), 
					accountCreation.getStrAccountNumber() 
				}
			);
			System.out.print("pre Cre Upadte "+ count + "with closing balance "+accountCreation.getStrClosingBalance() 
			+" and preCredRemainingAmount " + accountCreation.getStrPreCredAmount());
			
		/* return count; */
			
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	/* return 0; */
	}

	@Override
	public CustomerByAccountResponse getCustomerAccountInfoByAccountNo(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder querySb = new StringBuilder("SELECT am.cust_id as strCustId, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, am.account_number AS strAccountNumber, am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount, ");  
			querySb.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			querySb.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal, ");
			querySb.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			querySb.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			querySb.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			querySb.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,atym.gl_account_type AS strGLAccountType, ");
			querySb.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode, ");
			querySb.append("cm.active_tier AS strActiveTier,tam.tier1_cummulative_balance AS tier1CummBalance,tam.tier2_cummulative_balance AS tier2CummBalance, ");
			querySb.append("tam.tier3_cummulative_balance AS tier3CummBalance,tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			querySb.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit, ");
			querySb.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");
			
			querySb.append("FROM account_master AS am ");
			querySb.append("INNER JOIN customer_master AS cm ON TRIM(am.cust_id) = TRIM(cm.cust_id) ");
			querySb.append("INNER JOIN account_type_master AS atym ON TRIM(atym.account_type) = TRIM(am.account_type) ");
			querySb.append("INNER JOIN tier_account_master AS tam ON TRIM(tam.account_no) = TRIM(am.account_number) ");
			querySb.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no)");
			
			querySb.append("WHERE am.account_number='"+accountCreation.getStrAccountNumber()+"' ");


			List<CustomerByAccountResponse> customerAccountByNo = jdbcTemplate.query(querySb.toString(), new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] {});
			if (customerAccountByNo!=null && customerAccountByNo.size() > 0) 
			{
				return customerAccountByNo.get(0);
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public CustomerByAccountResponse getCustomerAccountInfoByAccountNoForNonNigeria(AccountCreation accountCreation)
	{
		try 
		{
			StringBuilder querySb = new StringBuilder();
			querySb.append("SELECT am.cust_id as strCustId, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, am.account_number AS strAccountNumber,am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount,");
			querySb.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			querySb.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			querySb.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			querySb.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			querySb.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			querySb.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,");
			querySb.append("glatm.account_type AS glAccountType, glatm.account_number AS glAccountNo, glatm.closing_balance AS glAccountBalance, glatm.account_description AS glAccountDescr ");
			
			querySb.append("FROM account_master AS am ");
			querySb.append("INNER JOIN customer_master AS cm ON TRIM(am.cust_id) = TRIM(cm.cust_id) ");
			querySb.append("INNER JOIN account_type_master AS atym ON TRIM(atym.account_type) = TRIM(am.account_type) ");
			querySb.append("INNER JOIN gl_account_type_master AS glatm ON TRIM(glatm.account_number) = TRIM(atym.gl_account_no)");
			
			querySb.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber()+"'");
			
			List<CustomerByAccountResponse> customerAccountByNo = jdbcTemplate.query(querySb.toString(),
					new BeanPropertyRowMapper<CustomerByAccountResponse>(CustomerByAccountResponse.class), new Object[] {});
			if (customerAccountByNo!=null && customerAccountByNo.size() > 0) 
			{
				return customerAccountByNo.get(0);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int[] batchEntryOfEarMarkUpdates(List<AccountCreation> accountCreationlist) throws Exception 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.ear_mark_amount = ?");
			updateQueryBuilder.append(" WHERE am.account_number = ? AND am.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, Utils.decimalFormat.format(accountCreationlist.get(i).getStrEarMarkAmount()));
					psmt.setString(2, accountCreationlist.get(i).getStrAccountNumber());
					psmt.setString(3, accountCreationlist.get(i).getStrAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] batchEntryOfPredCredUpdates(List<AccountCreation> accountCreationlist) throws Exception 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.pre_cred_amount = ?");
			updateQueryBuilder.append(" WHERE am.account_number = ? AND am.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, Utils.decimalFormat.format(accountCreationlist.get(i).getStrPreCredAmount()));
					psmt.setString(2, accountCreationlist.get(i).getStrAccountNumber());
					psmt.setString(3, accountCreationlist.get(i).getStrAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int updatePreCredAmountOfCustomerAccount(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.pre_cred_amount = ? ");
			updateQueryBuilder.append("WHERE am.account_number = ? AND am.account_type = ?");
			
			int count = this.jdbcTemplate.update(updateQueryBuilder.toString(),
					new Object[] 
					{ 	
						Utils.decimalFormat.format(accountCreation.getStrPreCredAmount()),
						accountCreation.getStrAccountNumber(),
						accountCreation.getStrAccountType()
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public int updateEarMarkAmountOfCustomerAccount(AccountCreation accountCreation)
	{
		try 
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.ear_mark_amount = ? ");
			updateQueryBuilder.append("WHERE am.account_number = ? AND am.account_type = ?");
			
			int count = this.jdbcTemplate.update(updateQueryBuilder.toString(),
					new Object[] 
					{ 	
						Utils.decimalFormat.format(accountCreation.getStrEarMarkAmount()),
						accountCreation.getStrAccountNumber(),
						accountCreation.getStrAccountType()
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public int[] updatesBatchEntryOfToAccount(List<AccountCreation> accountCreationlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.closing_balance = ?, am.pre_cred_amount = ? ");
			updateQueryBuilder.append("WHERE am.account_number = ? AND am.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountCreationlist.get(i).getStrClosingBalance());
					psmt.setString(2, Utils.decimalFormat.format(accountCreationlist.get(i).getStrPreCredAmount()));
					psmt.setString(3, accountCreationlist.get(i).getStrAccountNumber());
					psmt.setString(4, accountCreationlist.get(i).getStrAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] updatesBatchEntryOfFromAccount(List<AccountCreation> accountCreationlist)
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.closing_balance = ?, am.ear_mark_amount = ? ");
			updateQueryBuilder.append("WHERE am.account_number = ? AND am.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountCreationlist.get(i).getStrClosingBalance());
					psmt.setString(2, Utils.decimalFormat.format(accountCreationlist.get(i).getStrEarMarkAmount()));
					psmt.setString(3, accountCreationlist.get(i).getStrAccountNumber());
					psmt.setString(4, accountCreationlist.get(i).getStrAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;	
	}

	@Override
	public int[] updatesBatchEntryOfLimitsAccountMasterFields(List<AccountCreation> accountCreationlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am SET am.available_daily_limit = ?, am.available_monthly_limit = ?, am.available_yearly_limit = ? ");
			updateQueryBuilder.append("WHERE am.account_number = ? AND am.account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountCreationlist.get(i).getStrAvailableDailyLimit());
					psmt.setString(2, accountCreationlist.get(i).getStrAvailableMonthlyLimit());
					psmt.setString(3, accountCreationlist.get(i).getStrAvailableYearlyLimit());
					psmt.setString(4, accountCreationlist.get(i).getStrAccountNumber());
					psmt.setString(5, accountCreationlist.get(i).getStrAccountType());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
	
	@SuppressWarnings("unused")
	@Override
	public AccountCreation getAccountLimitInfoToCompareTransferAmount(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.account_number AS strAccountNumber, ");
			queryBuilder.append("am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
			queryBuilder.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			queryBuilder.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal,am.cust_id as strCustId, ");
			queryBuilder.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			queryBuilder.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			queryBuilder.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			queryBuilder.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,atym.gl_account_type AS strGLAccountType, ");
			queryBuilder.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode, ");
			queryBuilder.append("cm.active_tier AS strActiveTier,tam.tier1_cummulative_balance AS tier1CummBalance,tam.tier2_cummulative_balance AS tier2CummBalance, ");
			queryBuilder.append("tam.tier3_cummulative_balance AS tier3CummBalance,tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			queryBuilder.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit ");
			queryBuilder.append("FROM account_master AS am INNER JOIN customer_master AS cm ON am.cust_id = cm.cust_id ");
			queryBuilder.append("INNER JOIN account_type_master AS atym ON atym.account_type = am.account_type INNER JOIN tier_account_master AS tam ON tam.account_no = am.account_number ");
			queryBuilder.append("WHERE am.account_number = ? AND am.account_type = ? ");
			
			List<AccountCreation> accCreationList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
				 new Object[]  { 
						 accountCreation.getStrAccountNumber(),
						 accountCreation.getStrAccountType()
						});
			return accCreationList.get(0);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreation;		
	}

	@Override
	public AccountCreation getCumlativeBalanceCompareTransAmt(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.account_number AS strAccountNumber, ");
			queryBuilder.append("am.account_type AS strAccountType,am.ear_mark_amount AS strEarMarkAmount, ");
			queryBuilder.append("am.pre_cred_amount AS strPreCredAmount,am.closing_balance AS strClosingBalance,am.`status` AS strStatus, ");
			queryBuilder.append("am.load_count AS strLoadCount,am.total_outstanding_balance AS strTotalOutstandingBal,am.cust_id as strCustId, ");
			queryBuilder.append("am.total_available_grace_period AS strAvailableGracePeriod,am.available_credit_limit AS strAvailableCreditLimit, ");
			queryBuilder.append("am.available_daily_limit AS strAvailableDailyLimit,am.available_monthly_limit AS strAvailableMonthlyLimit, ");
			queryBuilder.append("am.available_yearly_limit AS strAvailableYearlyLimit,atym.account_type_category AS strAccountTypeCategory, ");
			queryBuilder.append("atym.allow_load_cash AS strAllowLoadCash,atym.`status` AS strTStatus,atym.gl_account_type AS strGLAccountType, ");
			queryBuilder.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode, ");
			queryBuilder.append("atym.gl_account_no AS strGLAccountNumber,atym.nuban_type AS strNubanType,atym.account_type_code AS strAccountTypeCode,");
			queryBuilder.append("cm.active_tier AS strActiveTier,tam.tier1_cummulative_balance AS strTier1CummulativeBalance,tam.tier2_cummulative_balance AS strTier2CummulativeBalance, ");
			queryBuilder.append("tam.tier3_cummulative_balance AS strTier3CummulativeBalance,tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			queryBuilder.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit, ");
			queryBuilder.append("tier1_daily_cum_limit AS strTier1DailyCumlimit, tier2_daily_cum_limit AS strTier2DailyCumlimit, tier3_daily_cum_limit AS strTier3DailyCumlimit ");
			queryBuilder.append("FROM account_master AS am INNER JOIN customer_master AS cm ON am.cust_id = cm.cust_id INNER JOIN account_type_master AS atym ");
			queryBuilder.append("ON atym.account_type = am.account_type INNER JOIN tier_account_master AS tam ON tam.account_no = am.account_number ");
			queryBuilder.append("WHERE am.account_number = ? AND am.account_type = ? ");
			
			List<AccountCreation> accCreationList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
				 new Object[]  { 
						 accountCreation.getStrAccountNumber(),
						 accountCreation.getStrAccountType()
						});
			return accCreationList.get(0);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreation;		
	}
	@Override
	public int updateAccountBalanceWithoutAccountType(AccountCreation accountCreation)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master SET closing_balance = ?,load_count = ? WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountCreation.getStrClosingBalance(),
						accountCreation.getStrLoadCount(),
						accountCreation.getStrAccountNumber()
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeInfoLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	//added by sunil Y , 2023-07-10 started
	@Override
	public AccountMaster getAccountTypeIsExist(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.account_type as strAccountType , am.`status` as strStatus From account_master AS am  WHERE am.cust_id = ?  And  am.account_type = ? ");
			
			List<AccountMaster> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class), new Object[]  
			{ 
			    		 accountMaster.getStrCustId(),
			    		 accountMaster.getStrAccountType()
			});
			amsLogger.writeInfoLog(queryBuilder.toString());
			if (accountCretion!=null && accountCretion.size() > 0) 
			{
				return accountCretion.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public AccountMaster getAccountTypeIsActiveOnAccountNumber(AccountMaster accountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.account_type as strAccountType , am.`status` as strStatus From account_master AS am  WHERE am.cust_id= ?  And  am.account_number = ? ");
			
			List<AccountMaster> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			     new Object[]  { 
			    		 accountMaster.getStrCustId(),
			    		 accountMaster.getStrAccountType()
			});
			amsLogger.writeInfoLog(queryBuilder.toString());
			if (accountCretion!=null && accountCretion.size() > 0) 
			{
				return accountCretion.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateAccountStatusByAccountId(AccountCreation accountMaster) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "active = ? "
					+ "WHERE account_number = ? ",
					new Object[] 
					{ 	
						accountMaster.getStrAccountNumber() 
					});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;	
	}

	@Override
	public AccountResponse getAccountMasterInformation(AccountCreation accountCreation) 
	{
		try 
		{
			if (accountCreation.getStrAccountNumber()!=null && accountCreation.getStrAccountNumber().trim().length() > 0)
			{
				StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT am.id AS strID, am.cust_id AS strCustId, am.mobile_no AS strMobileNo, am.account_number AS strAccountNumber, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
				queryBuilder.append("atm.account_type_category AS accountCategoryType, am.account_type AS strAccountType, am.status as strStatus, atm.status AS strAccounTypeStatus, am.email AS strEmailID,");
				queryBuilder.append("am.closing_balance AS strClosingBalance, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount,");
				
				queryBuilder.append("glatm.account_type AS strGLAccountType, glatm.account_number AS strGLAccountNo, glatm.account_description AS strGLAccountDescription, glatm.closing_balance AS strGLAccountBalance, ");
				
				queryBuilder.append("cm.active_tier AS strActiveTier, tam.tier1_daily_cum_limit AS strTier1DailyCumLimit, tam.tier2_daily_cum_limit AS strTier2DailyCumLimit, tam.tier3_daily_cum_limit AS strTier3DailyCumLimit,");
				queryBuilder.append("tam.available_tier1_daily_cum_limit AS availableTier1DailyCumLimit, tam.available_tier2_daily_cum_limit AS availableTier2DailyCumLimit,");				
				queryBuilder.append("tam.available_tier3_daily_cum_limit AS availableTier3DailyCumLimit, tam.tier1_cummulative_balance AS tier1CumBalance,");				
				queryBuilder.append("tam.tier2_cummulative_balance AS tier2CumBalance, tam.tier3_cummulative_balance AS tier3CumBalance,");				
				
				queryBuilder.append("am.per_txn_limit AS strSingleTxnLimit, am.daily_txn_limit AS strDailyTxnLimit, am.monthly_txn_limit AS strMonthlyTxnLimit, am.yearly_txn_limit AS strYearlyTxnLimit, ");				
				queryBuilder.append("am.available_daily_limit AS strAvailableDailyLimit, am.available_monthly_limit AS strAvailableMonthlyLimit, am.available_yearly_limit AS strAvailableYearlyLimit, ");
				queryBuilder.append("am.credit_limit_amount AS strCreditLimitAmount, am.available_credit_limit AS strAvailableCreditLimit, atm.is_revolving_credit AS strIsRevolvingCredit, ");
				queryBuilder.append("rccm.grace_period_in_days AS revolvingGracePeriodInDays, am.total_outstanding_balance AS strTotalOutstandingBal, rccm.billing_cycle_date AS strBillingCycleDate ");
				
				queryBuilder.append("FROM account_master AS am ");
				queryBuilder.append("INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
				queryBuilder.append("LEFT JOIN revolving_credit_card_master rccm ON rccm.account_type = am.account_type ");
				
				queryBuilder.append("LEFT JOIN gl_account_type_master glatm ON glatm.account_type = atm.gl_account_type ");
				queryBuilder.append("LEFT JOIN customer_master cm ON cm.cust_id = am.cust_id ");
				queryBuilder.append("LEFT JOIN tier_account_master tam ON tam.account_no = am.account_number ");
				
				if (accountCreation.getStrCustId()!=null && accountCreation.getStrCustId().trim().length() > 0) 
				{
					queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber().trim()+"' AND am.cust_id = '"+accountCreation.getStrCustId().trim()+"'");
				}
				else
				{
					queryBuilder.append("WHERE am.account_number = '"+accountCreation.getStrAccountNumber().trim()+"' AND am.cust_id = (SELECT cust_id FROM account_master sbam WHERE sbam.account_number = '"+accountCreation.getStrAccountNumber().trim()+"')");
				}
				
				amsLogger.writeInfoLog("getAccountMasterInformation:: query=["+queryBuilder.toString()+"]");
				
				List<AccountResponse> accountMasterResponses  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountResponse>(AccountResponse.class), new Object[] {});
				if (accountMasterResponses!=null && accountMasterResponses.size() > 0) 
				{
					return accountMasterResponses.get(0);
				}	
			}			
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;	
	}
	
	@Override
	public List<AccountCreation> getAccoutBalanceList(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT '"+accountCreation.getDate()+"' AS date, '"+accountCreation.getTime()+"' AS time, ");
					queryBuilder.append("am.account_type AS strAccountType, am.account_number AS strAccountNumber,");
					queryBuilder.append(" am.cust_id AS strCustId, mam.cid AS cid ,IFNULL(mam.bid,'-') AS bid, am.closing_balance AS strClosingBalance,");
					queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName ");
					queryBuilder.append("FROM account_master am ");
					queryBuilder.append("INNER JOIN montra_account_master mam ");
					queryBuilder.append("ON am.account_number = mam.account_number ");
					queryBuilder.append("Where am.cust_id = mam.cust_id ");
			  
			  List<AccountCreation> getAccountBalanceList = jdbcTemplate.query(queryBuilder.toString(),
						new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
						new Object[] { 
						}
						);
		
		       return getAccountBalanceList;
	   }
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		amsLogger.writeInfoLog("AccountMasterDaoImpl.getAccoutBalanceList()"+e);
		amsLogger.writeInfoLog("Exception in getAccoutBalanceList::"+e);
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
		return null;
	}
	
	@Override
	public List<AccountCreation> getAccoutInfoListbyCustId(AccountCreation accountCreation) {
		try {
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.mcc_code as mccCode, ");
					queryBuilder.append("am.account_type AS strAccountType, am.account_number AS strAccountNumber, ");
					queryBuilder.append("am.closing_balance AS strClosingBalance, ");
					queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName ");
					queryBuilder.append("FROM account_master am ");
					queryBuilder.append("INNER JOIN montra_account_master mam ");
					queryBuilder.append("ON am.account_number = mam.account_number ");
					queryBuilder.append("Where am.cust_id = ? AND mam.cid = ? ");
					if(!"ALL".equals(accountCreation.getStrAccountType()) && accountCreation.getStrAccountType() != null)
					{
						queryBuilder.append("AND am.account_type = '"+ accountCreation.getStrAccountType() +"' ");
					}
			  List<AccountCreation> getAccountList = jdbcTemplate.query(queryBuilder.toString(),
						new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
						new Object[] { 
								accountCreation.getStrCustId(),
								accountCreation.getCid()
						}
						);
			  if(getAccountList != null && getAccountList.size() > 0)
			  {
				  return getAccountList;
			  }
	   }
	catch (Exception e) 
	{
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		amsLogger.writeInfoLog("AccountMasterDaoImpl::"+e);
		amsLogger.writeInfoLog("Exception in getAccoutInfoListbyCustId::"+e);
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
		return null;
	}
	
	@Override
	public List<AccountCreation> getAccoutInfoListByAccountType(AccountCreation accountCreation) 
	{
		try
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT mam.mcc_code as mccCode, atm.category_type AS strCategoryType, atm.account_type_category AS atmcategory, ");
			queryBuilder.append("am.account_type AS strAccountType, am.account_number AS strAccountNumber, ");
			queryBuilder.append("am.closing_balance AS strClosingBalance, ");
			queryBuilder.append("CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName ");
			queryBuilder.append("FROM account_master am INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			queryBuilder.append("INNER JOIN montra_account_master mam ");
			queryBuilder.append("ON am.account_number = mam.account_number ");
			queryBuilder.append("Where am.cust_id = '"+accountCreation.getStrCustId()+"' AND mam.cid = '"+accountCreation.getCid()+"' ");
					
			if(!"ALL".equalsIgnoreCase(accountCreation.getAccountCategoryType()) && accountCreation.getAccountCategoryType() != null)
			{
					queryBuilder.append("AND atm.category_type = '"+ accountCreation.getAccountCategoryType() +"' ");
			}
					
			List<AccountCreation> getAccountList = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class), new Object[] {});
			  
			if(getAccountList != null && getAccountList.size() > 0)
			{
				  return getAccountList;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	// Added by Sunil Y , For Dormancy Process , Start
	@Override
	public List<AccountCreation> getActiveAccountList(AccountCreation accountCreation) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.cust_id AS strCustId, am.account_type AS strAccountType, am.account_number AS strAccountNumber,");
			queryBuilder.append("am.closing_balance AS strClosingBalance, am.creation_date AS strDateOfCreation,  ");
			queryBuilder.append("am.last_successfull_txn_date AS lastTxnDate, atm.dormancy_periods_in_days AS dormancyPeriodsIndays ");
			queryBuilder.append("FROM account_master AS am INNER JOIN account_type_master AS atm ");
			queryBuilder.append("ON atm.account_type = am.account_type WHERE am.`status` = '"+accountCreation.getStrStatus().trim()+"' ");
			
			List<AccountCreation> getAccountList = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class), new Object[] {});
			if(getAccountList != null && getAccountList.size() > 0)
			{
				  return getAccountList;
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	// Added by Sunil Y , For Dormancy Process , End

	@Override

	public int updateAccountMasterForDormancy(AccountCreation accountCreation)
	{
		try
		{
			int count = this.jdbcTemplate.update("UPDATE account_master SET status = ?, dormant_marked_date = ? WHERE account_number = ? ",
			new Object[]
			{ 	
				accountCreation.getStrStatus(),
				accountCreation.getDormantMarkedDate(),
				accountCreation.getStrAccountNumber()
			});
			amsLogger.writeInfoLog("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e)
		{
			amsLogger.writeInfoLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public int updateAccountMasterForDormant(AccountCreation accountCreation) 
	{
		String sqlUpdateQuery = "UPDATE account_master SET dormant_marked_date = ? , dormant_released_date = ? , status = ?  WHERE account_number = ? ";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] 
			{
				accountCreation.getDormantMarkedDate(),
				accountCreation.getDormantReleasedDate(),
				accountCreation.getStrStatus(),
				accountCreation.getStrAccountNumber()
			});
			
			amsLogger.writeInfoLog("Inside updateAccountMasterForDormant query:::"+sqlUpdateQuery);
			return count;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public void updateLastTxnDate(AccountCreation accountCreation) 
	{
		String sqlUpdateQuery = "UPDATE account_master SET last_successfull_txn_date = ? WHERE account_number = ? ";
		try 
		{
			this.jdbcTemplate.update(sqlUpdateQuery, new Object[] 
			{
						accountCreation.getLastTxnDate(),
						accountCreation.getStrAccountNumber()
			});
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public int[] updatesBatchEntryOfDormancyAccountMasterFields(List<AccountCreation> accountCreationlist) 
	{

		int[] batchResponse = null;
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master SET status = ?, dormant_marked_date = ? WHERE account_number = ?");
			
			return this.jdbcTemplate.batchUpdate(updateQueryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountCreationlist.get(i).getStrStatus());
					psmt.setDate(2, Utils.getSqlDate(accountCreationlist.get(i).getDormantMarkedDate()));
					psmt.setString(3, accountCreationlist.get(i).getStrAccountNumber());
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreationlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;	
	
	}

	@Override
	public List<AccountMaster> getAllActiveAccount() {
		List<AccountMaster> accountMasters = new ArrayList<>();
		try 
		{
			StringBuilder querysb = new StringBuilder(" SELECT am.account_type AS strAccountType , am.account_number AS strAccountNumber ");
			 querysb.append(" FROM account_master AS am WHERE am.`status`='Active'");
					
			 accountMasters = this.jdbcTemplate.query(querysb.toString(), 
						(RowMapper<AccountMaster>) new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			 new Object[] 
					{ 	
						
					});
			
			return accountMasters;
		}
		catch (Exception e) 
		{
		
		}
		return accountMasters;	
	}


	


	@Override
	public AccountMaster getBaseWalletAccount(AccountMaster baseWallet) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, am.status AS strStatus, am.account_number AS strAccountNumber, atym.account_type_category AS strAccountTypeCategory, am.first_name AS strFirstName, ");
			queryBuilder.append("email AS strEmailID , am.currency_code AS currencyCode , am.middle_name AS strMiddleName, am.last_name AS strLastName, atym.allow_load_cash AS strAllowLoadCash, am.load_count AS strLoadCount, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount, am.account_type AS strAccountType,");
			queryBuilder.append("am.closing_balance AS strClosingBalance, atym.category_type AS strAccountCategory, atym.gl_account_type AS strGLAccountType from account_master am INNER JOIN account_type_master atym ");
			queryBuilder.append("ON am.account_type = atym.account_type ");
			queryBuilder.append("WHERE am.account_number = '"+baseWallet.getStrAccountNumber()+"'");
			
			List<AccountMaster> accountMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<AccountMaster>(AccountMaster.class),
			     new Object[]  {});
			
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			//amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public void updateBaseWalletAccountMaster(AccountMaster baseWalletAccount) {
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE account_master am ");
			updateQueryBuilder.append( "SET am.closing_balance = '"+baseWalletAccount.getStrClosingBalance()+"' ");
			updateQueryBuilder.append("WHERE am.account_number = '"+baseWalletAccount.getStrAccountNumber()+"'");
			
			int update = this.jdbcTemplate.update(updateQueryBuilder.toString(), new Object[] {});
			
		}
		catch (Exception e) 
		{
			}
		
	}

	@Override
	public AccountCreation getAccountDetails(AccountCreation accountMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT am.id AS strID, am.cid AS cid, am.bid AS bid, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, am.status AS strStatus, am.account_number AS strAccountNumber, atym.account_type_category AS strAccountTypeCategory, am.first_name AS strFirstName, ");
			queryBuilder.append("email AS strEmailID, am.middle_name AS strMiddleName, am.last_name AS strLastName, atym.allow_load_cash AS strAllowLoadCash, am.load_count AS strLoadCount, am.pre_cred_amount AS strPreCredAmount, am.ear_mark_amount AS strEarMarkAmount, am.account_type AS strAccountType,");
			queryBuilder.append("am.closing_balance AS strClosingBalance, atym.category_type AS strAccountCategory, atym.gl_account_type AS strGLAccountType,atym.is_Channel AS isChannel ,atym.is_multi_currency_support AS isMultiCurrencySupport from account_master am INNER JOIN account_type_master atym ");
			queryBuilder.append("ON am.account_type = atym.account_type ");
			queryBuilder.append("WHERE am.account_number = '"+accountMaster.getStrAccountNumber()+"'");
			
			if (accountMaster.getStrAccountType()!=null && accountMaster.getStrAccountType().trim().length() > 0)
			{
				queryBuilder.append("  and am.account_type = '"+accountMaster.getStrAccountType()+"'");
			}
			
			amsLogger.writeInfoLog("In getAccountDetails Query::"+queryBuilder.toString());
			
			List<AccountCreation> accountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


}
