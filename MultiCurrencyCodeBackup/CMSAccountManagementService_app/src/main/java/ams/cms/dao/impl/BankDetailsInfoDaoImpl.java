package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

import ams.cms.api.model.BankListResponse;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.dao.BankDetailsInfoDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.BankDetailsInfo;
import ams.cms.utility.BankMiddleWareData;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.ExternalServerTokenModel;
import ams.cms.utility.MiddleWareTokenModel;
import ams.cms.utility.NameEnquiryRequestResponseModel;

@Repository
public class BankDetailsInfoDaoImpl extends AbstractGenericDao<BankDetailsInfo> implements BankDetailsInfoDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BankDetailsInfoDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void truncateTable(String tableName) 
	{
	    String sql = "TRUNCATE TABLE " + tableName;
	    jdbcTemplate.execute(sql);
	}
	
	@Override
	public int[] batchEntryOfBankDetailsInfoFromNIBSSS(List<BankDetailsInfo> baDetailsInfos) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO bank_details_info "
					+ "(bank_name, bank_code, nip_code, active, abc_sort_code, is_ofi, creation_date_time) "
					+ "values(?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, baDetailsInfos.get(i).getStrBankName());
							psmt.setString(2, baDetailsInfos.get(i).getStrBankCode());
							psmt.setString(3, baDetailsInfos.get(i).getStrNipCode());
							psmt.setString(4, baDetailsInfos.get(i).getStrActive());
							psmt.setString(5, baDetailsInfos.get(i).getStrIsOfi());
							psmt.setString(6, baDetailsInfos.get(i).getStrAbcSortCode());
							psmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
							
						}
						
						@Override
						public int getBatchSize() 
						{
							return baDetailsInfos.size();
						}
					});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int addBankRelatedToken(ExternalServerTokenModel externalServerTokenModel) 
	{
		StringBuilder insertSb = new StringBuilder("INSERT INTO nibss_token_info (access_token, token_active, token_id) VALUES (?,?,?)");
		int count = this.jdbcTemplate.update(insertSb.toString(),
				new Object[] 
				{ 
						externalServerTokenModel.getAccess_token(),
						externalServerTokenModel.getToken_active(),
						externalServerTokenModel.getToken_id()
				});
		return count;
	}

	@Override
	public int updateankRelatedToken(ExternalServerTokenModel externalServerTokenModel) 
	{
		int updateResult = 0;
		try
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE nibss_token_info SET ");
			updateQuery.append("token_active = ? ");
			updateQuery.append("WHERE token_id = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[]
			{ 
				externalServerTokenModel.getToken_active(),
				externalServerTokenModel.getToken_id()
			});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updateResult;
	
	}

	@Override
	public String getAccessToken(ExternalServerTokenModel externalServerTokenModel) 
	{
		try 
		{
			String sql = "SELECT nti.access_token AS access_token FROM nibss_token_info nti WHERE nti.token_active='A' AND nti.token_id = ?";
			String accessTokenStr = this.jdbcTemplate.queryForObject(sql, String.class, new Object[] {externalServerTokenModel.getToken_id()} );
			if (accessTokenStr!=null && accessTokenStr.trim().length() > 0) 
			{
				accessTokenStr = accessTokenStr.trim();
			}
			return accessTokenStr;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankListResponse> getNibssBankList() 
	{
		try 
		{
			String sql = "SELECT bdi.nip_code AS bankCode, bdi.bank_name AS bankName FROM bank_details_info bdi";
			@SuppressWarnings("rawtypes")
			List<BankListResponse> bankListResponses = this.jdbcTemplate.query(sql,
					 (RowMapper) new BeanPropertyRowMapper(BankListResponse.class),
						new Object[] {} 
					);
			return bankListResponses;
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int addNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		StringBuilder insertSb = new StringBuilder("INSERT INTO name_enquiry_req_res ");
		insertSb.append("(account_number, account_name, channel_code, destination_institution_code, ");
		insertSb.append("transaction_id, session_id, bank_verification_number, kyc_level, response_code, message)");
		insertSb.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");
		int count = this.jdbcTemplate.update(insertSb.toString(),
		new Object[] 
		{ 
				externalServerRequestResponseModel.getAccountNumber(),
				externalServerRequestResponseModel.getAccountName(),
				externalServerRequestResponseModel.getChannelCode(),
				externalServerRequestResponseModel.getDestinationInstitutionCode(),
				externalServerRequestResponseModel.getTransactionId(),
				externalServerRequestResponseModel.getSessionID(),
				externalServerRequestResponseModel.getBankVerificationNumber(),
				externalServerRequestResponseModel.getKycLevel(),
				externalServerRequestResponseModel.getResponseCode(),
				externalServerRequestResponseModel.getMessage()
		});
		return count;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ExternalServerRequestResponseModel getNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		try
		{
			String sql = "select session_id as sessionID from name_enquiry_req_res WHERE transaction_id = ?";
			List<ExternalServerRequestResponseModel> externalServerRequestResponseModels = this.jdbcTemplate.query(sql,
			(RowMapper) new BeanPropertyRowMapper(ExternalServerRequestResponseModel.class), 
			new Object[] 
			{ 
				externalServerRequestResponseModel.getTransactionId() 
			});
			
			if (externalServerRequestResponseModels != null && externalServerRequestResponseModels.size() > 0) 
			{
				externalServerRequestResponseModel = externalServerRequestResponseModels.get(0);
			}
			return externalServerRequestResponseModel;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updaNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		int updateResult = 0;
		try 
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE name_enquiry_req_res SET ");
			updateQuery.append("account_name = ?, session_id = ?, bank_verification_number = ?, ");
			updateQuery.append("kyc_level = ?, response_code = ?, message = ? ");
			updateQuery.append("WHERE transaction_id = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[] 
			{ 
				externalServerRequestResponseModel.getAccountName(),
				externalServerRequestResponseModel.getSessionID(),
				externalServerRequestResponseModel.getBankVerificationNumber(),
				externalServerRequestResponseModel.getKycLevel(),
				externalServerRequestResponseModel.getResponseCode(),
				externalServerRequestResponseModel.getMessage(), 
				externalServerRequestResponseModel.getTransactionId()
			});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updateResult;
	}


	
	//middleWare APi for get Bank List
	
	
	@Override
	public int addBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) {
		StringBuilder insertSb = new StringBuilder("INSERT INTO middleware_token_info (middleware_token, active_token, token_id) VALUES (?,?,?)");
		int count = this.jdbcTemplate.update(insertSb.toString(),
				new Object[] 
				{ 
					middleWareTokenModel.getMiddleware_token(),
					middleWareTokenModel.getToken_active(),
					middleWareTokenModel.getToken_id()
				});
		return count;
		
	}

	
	@Override
	public int updateBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) 
	{
		int updateResult = 0;
		try
		{
			StringBuilder updateQuery = new StringBuilder("UPDATE middleware_token_info SET ");
			updateQuery.append("token_active = ? ");
			updateQuery.append("WHERE token_id = ?");
			
			updateResult = this.jdbcTemplate.update(updateQuery.toString(),
			new Object[]
			{ 
				middleWareTokenModel.getToken_active(),
				middleWareTokenModel.getToken_id()
			});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updateResult;
	}
	
	@Override
	public String getMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) {
		try 
		{
			String sql = "SELECT mti.middleware_token AS middleware_token FROM middleware_token_info mti WHERE mti.token_active='A' AND mti.token_id = ?";
			String middleWaretoken = this.jdbcTemplate.queryForObject(sql, String.class, new Object[] {middleWareTokenModel.getToken_id()} );
			if (middleWaretoken!=null && middleWaretoken.trim().length() > 0) 
			{
				middleWaretoken = middleWaretoken.trim();
			}
			return middleWaretoken;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
		
	}


	@Override
	public int[] batchEntryOfBankDetailsInfoFromMiddleWare(List<BankMiddleWareData> banksDetails) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder insertSb = new StringBuilder("INSERT INTO bank_details_info ");
			insertSb.append("(bank_code, nip_code, bank_category, bank_name, creation_date_time) ");
			insertSb.append("values(?,?,?,?,?)");
			
			return this.jdbcTemplate.batchUpdate(insertSb.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, banksDetails.get(i).getBankCode());
					psmt.setString(2, banksDetails.get(i).getCode());
					psmt.setString(3, banksDetails.get(i).getCategory());
					psmt.setString(4, banksDetails.get(i).getName());
					psmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				}
				
				@Override
				public int getBatchSize() 
				{
					return banksDetails.size();
				}
			});
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured in batchEntryOfBankDetailsInfoFromMiddleWare::"+ExceptionUtils.getStackTrace(e));
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MiddleWareBankRequestModel getRequestDataForBank(MiddleWareBankRequestModel middleWareBankRequestModel) {
		try
		{
			StringBuilder selectSb = new StringBuilder("select mia.participant_id AS participantId, mia.app_id AS appId, mia.audit_id AS auditId, mia.initial_value AS strInitialValue, ");
			selectSb.append("mia.last_audit_ser_no AS strLastAuditSerNo, mia.year AS strYear, mia.channel_code AS channelCode, mia.user_loginid AS userLoginId ");
			selectSb.append("from middleware_app_info AS mia WHERE mia.participant_id = ? ");
			
			//String sql = "select app_id AS appId, audit_id AS auditId from middleware_app_info WHERE participant_id = ? ";
			List<MiddleWareBankRequestModel> middleWareRequestModels = this.jdbcTemplate.query(selectSb.toString(), (RowMapper) new BeanPropertyRowMapper(MiddleWareBankRequestModel.class), 
			new Object[] 
			{ 
				middleWareBankRequestModel.getParticipantId() 
			});
			return middleWareRequestModels.get(0);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		//return middleWareBankRequestModel;
		return null;
	}


	@Override
	public int updateNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel) 
		{
			int updateResult = 0;
			try 
			{
				StringBuilder updateQuery = new StringBuilder("UPDATE name_enquiry_req_res SET ");
				updateQuery.append("account_name = ?, session_id = ?, bank_verification_number = ?, ");
				updateQuery.append("kyc_level = ?, response_code = ?, message = ? ");
				updateQuery.append("WHERE transaction_id = ?");
				
				updateResult = this.jdbcTemplate.update(updateQuery.toString(),
				new Object[] 
				{ 
					nameEnquiryRequestResponseModel.getAccountName(),
					nameEnquiryRequestResponseModel.getSessionID(),
					nameEnquiryRequestResponseModel.getBankVerificationNumber(),
					nameEnquiryRequestResponseModel.getKycLevel(),
					nameEnquiryRequestResponseModel.getResponseCode()
				});
			}
			catch (Exception e) 
			{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			return updateResult;
		
	}


	@Override
	public int addNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel) 
	{
			StringBuilder insertSb = new StringBuilder("INSERT INTO name_enquiry_req_res ");
			insertSb.append("(account_number, account_name, channel_code, destination_institution_code, ");
			insertSb.append("session_id, bank_verification_number, kyc_level, response_code, customer_no, currency_code)");
			insertSb.append(" VALUES (?,?,?,?,?,?,?,?,?,?)");
			int count = this.jdbcTemplate.update(insertSb.toString(),
			new Object[] 
			{ 
				nameEnquiryRequestResponseModel.getAccountNumber(),
				nameEnquiryRequestResponseModel.getAccountName(),
				nameEnquiryRequestResponseModel.getChannelCode(),
				nameEnquiryRequestResponseModel.getDestinationInstitutionCode(),
				nameEnquiryRequestResponseModel.getSessionID(),
				nameEnquiryRequestResponseModel.getBankVerificationNumber(),
				nameEnquiryRequestResponseModel.getKycLevel(),
				nameEnquiryRequestResponseModel.getResponseCode(),
				nameEnquiryRequestResponseModel.getCustomerNo(),
				nameEnquiryRequestResponseModel.getCurrencyCode()
					
			});
			return count;
		}

	@Override
	public BankDetailsInfo getBankDetailsInfoFromBankCode(String bankcode) 
	{
		try
		{
			if (bankcode!=null && bankcode.trim().length() > 0)
			{
				Criteria criteria = createEntityCriteria();
				criteria.add(Restrictions.eq("strBankCode", bankcode));
				
				List<BankDetailsInfo> bankListObj = (List<BankDetailsInfo>) criteria.list();
				if (bankListObj !=null && bankListObj.size() > 0) 
				{
					return bankListObj.get(0);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}	
}
