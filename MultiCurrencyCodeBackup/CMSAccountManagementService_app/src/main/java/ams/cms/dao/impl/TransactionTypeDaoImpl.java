package ams.cms.dao.impl;


import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.TransactionTypeDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TransactionTypeModel;

@Repository
public class TransactionTypeDaoImpl extends AbstractGenericDao<TransactionTypeModel> implements TransactionTypeDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionTypeDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public TransactionTypeModel getTransactionTypeMaster(TransactionTypeModel transactionTypeModel)
	{
		try
		{
			amsLogger.writeExceptionLog("Inside getTransactionTypeMaster transactionTypeModel::"+transactionTypeModel);
			boolean isCrteriaExecute = false;
			Criteria criteria = createEntityCriteria();
			if (transactionTypeModel.getStrTxnTypeKeyWord()!=null && transactionTypeModel.getStrTxnTypeKeyWord().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strTxnTypeKeyWord", transactionTypeModel.getStrTxnTypeKeyWord().trim()));
				isCrteriaExecute = true;
			}
			if (transactionTypeModel.getStrProcessingCode()!=null && transactionTypeModel.getStrProcessingCode().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strProcessingCode", transactionTypeModel.getStrProcessingCode().trim()));
				isCrteriaExecute = true;
			}
			if (transactionTypeModel.getStrTxnTypeId()!=null && transactionTypeModel.getStrTxnTypeId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strTxnTypeId", transactionTypeModel.getStrProcessingCode().trim()));
				isCrteriaExecute = true;
			}
			
			amsLogger.writeInfoLog("--Get Transaction TYPE isCrteriaExecute::"+isCrteriaExecute);
			if (isCrteriaExecute )
			{
				List<TransactionTypeModel> transactionTypeModels = (List<TransactionTypeModel>) criteria.list();
				if (transactionTypeModels !=null && transactionTypeModels.size() > 0) 
				{
					amsLogger.writeInfoLog("--Get Transaction TYPE getTransactionTypeMaster::"+transactionTypeModels);
					return transactionTypeModels.get(0);
				}
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getTranTypeByProcessinCode(String proceessingCode) 
	{
		try 
		{
		
			String txnType = this.jdbcTemplate.queryForObject("select txn_type_keyword from transaction_type_master where processing_code = ?",
					new Object[] { proceessingCode }, String.class);
			System.out.println("txnType::" + txnType);
			return txnType;
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
		}
	
	@Override
	public TransactionTypeModel getGlAccountNoByTxnKeyword(TransactionTypeModel transactionTypeModel)
	{
		TransactionTypeModel respTransactionTypeModel = null;
		try 
		{
			amsLogger.writeInfoLog("In getGlAccountNoByTxnKeyword transactionTypeModel::["+transactionTypeModel+"]");
			if (transactionTypeModel == null)
			{
				return null;
			}
			if(transactionTypeModel.getStrTxnTypeKeyWord() == null) {
				return null;
			}
			String txnType = transactionTypeModel.getStrTxnTypeKeyWord().trim();			
			amsLogger.writeInfoLog("In getGlAccountNoByTxnKeyword txnType::["+txnType+"]");
			
			StringBuilder sql = new StringBuilder("SELECT ttm.txn_type_id AS strTxnTypeId, ttm.txn_type_description AS strTxnTypeDescrp, ");
			sql.append("ttm.gl_account_type AS strGLAccountType, ttm.gl_account_no AS strGLAccountNumber from ");
			sql.append("transaction_type_master AS ");
			sql.append("ttm WHERE ttm.txn_type_keyword LIKE '%"+txnType+"%'");
			
			List<TransactionTypeModel> transactionTypeModelwithGl  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<TransactionTypeModel>(TransactionTypeModel.class), new Object[] {});
			if(transactionTypeModelwithGl != null)
			{
				//return transactionTypeModelwithGl.get(0);
				respTransactionTypeModel = transactionTypeModelwithGl.get(0);
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("In getGlAccountNoByTxnKeyword respTransactionTypeModel::["+respTransactionTypeModel+"]");
		return respTransactionTypeModel;
	}

	@Override
	public TransactionTypeModel getTransactionTypeMasterBasedOnTxnType(TransactionTypeModel transactionTypeModel) 
	{
		TransactionTypeModel respTxnTypeModel = null;
		try 
		{
			if (transactionTypeModel == null)
			{
				return null;
			}
			if(transactionTypeModel.getStrTxnTypeKeyWord() == null) {
				return null;
			}
			String txnType = transactionTypeModel.getStrTxnTypeKeyWord().trim();
			
			amsLogger.writeInfoLog("In getTransactionTypeMasterBasedOnTxnType txnType::["+txnType+"]");
			
			StringBuilder sql = new StringBuilder("SELECT ttm.participant_id AS strParticipantId, ttm.txn_type_id AS strTxnTypeId, ttm.txn_type_description AS strTxnTypeDescrp, ");
			sql.append("ttm.gl_account_type AS strGLAccountType, ttm.gl_account_no AS strGLAccountNumber,");
			sql.append("ttm.processing_code AS strProcessingCode, ttm.txn_type_keyword AS strTxnTypeKeyWord ");
			sql.append("from transaction_type_master AS ttm ");
			sql.append("WHERE ttm.txn_type_keyword LIKE '%"+txnType+"%'");
			
			List<TransactionTypeModel> transactionTypeModelwithGl  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<TransactionTypeModel>(TransactionTypeModel.class), new Object[] {});
			if(transactionTypeModelwithGl != null)
			{
				respTxnTypeModel = transactionTypeModelwithGl.get(0);
				//return transactionTypeModelwithGl.get(0);
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return respTxnTypeModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<TransactionTypeModel> getTransactionTypeData(TransactionTypeModel transactionTypeModel) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append("SELECT ttm.txn_type_id AS strTxnTypeId,ttm.txn_type_description AS strTxnTypeDescrp, ");
			sqlQuery.append("ttm.txn_type_keyword AS strTxnTypeKeyWord,ttm.processing_code AS strProcessingCode, ");
			sqlQuery.append("CONCAT(ttm.gl_account_type,'-',ttm.gl_account_no) AS strGLAccountType ");
			sqlQuery.append("FROM transaction_type_master ttm ");
			
			List<TransactionTypeModel> transactiontTypeModel = this.jdbcTemplate.query(sqlQuery.toString(),
				(RowMapper) new BeanPropertyRowMapper(TransactionTypeModel.class),
				new Object[] 
				{
				}
			);
			return transactiontTypeModel;
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public boolean isTransactionTypeAlreadyExist(TransactionTypeModel transactionTypeModel) {
		try
		{
			String sql = "SELECT count(*) AS count FROM transaction_type_master "
					+ "WHERE txn_type_id = ? OR txn_type_keyword = ? OR processing_code = ?  ";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] 
					{ transactionTypeModel.getStrTxnTypeId(),
							transactionTypeModel.getStrTxnTypeKeyWord(),
							transactionTypeModel.getStrProcessingCode(),
							});
			return count > 0;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isGLAccountTypeExist(TransactionTypeModel transactionTypeModel) 
	{
		try
		{
			String sql = "SELECT count(*) AS count FROM transaction_type_master "
					+ "WHERE  gl_account_type = ? OR gl_account_no = ? ";
			int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] 
					{ 
						transactionTypeModel.getStrGLAccountType(),
						transactionTypeModel.getStrGLAccountNumber()});
			return count > 0;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
}
