package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.DormantMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.DormantAccountMaster;
import ams.cms.utility.Utils;

@Repository
public class DormantMasterDaoImpl extends AbstractGenericDao<DormantAccountMaster> implements DormantMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(DormantMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public DormantAccountMaster getAccountInformantion(DormantAccountMaster dormantAccountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName  , atm.account_type , atm.description AS description , am.creation_date AS creationDate , am.last_txn_date AS lastTxnDate , ");
			queryBuilder.append("am.closing_balance AS closingBalance , am.dormant_marked_date AS dormantMarkedDate ,am.status FROM account_type_master atm INNER JOIN account_master am ");
			queryBuilder.append("ON atm.account_type = am.account_type WHERE am.account_number =   '"+dormantAccountMaster.getAccountNumber()+"'");
			
			List<DormantAccountMaster> dormantAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<DormantAccountMaster>(DormantAccountMaster.class), new Object[]  {});
			if (dormantAccountMasters!=null && dormantAccountMasters.size() > 0) 
			{
				return dormantAccountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateDormantAccountMasterByAccountNo(DormantAccountMaster dormantAccountMasterObj) 
	{
		String sqlUpdateQuery = "UPDATE dormant_account_master SET account_dormant_released_date = ?, dormant_released_reason = ?, released_by = ? WHERE account_number = ? ";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] 
			{
				dormantAccountMasterObj.getAccountDormantReleasedDate(),
				dormantAccountMasterObj.getDormantReleasedReason(), 
				dormantAccountMasterObj.getReleasedBy(),
				dormantAccountMasterObj.getAccountNumber()
			});
			
			System.out.println(sqlUpdateQuery);
			return count;

		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public DormantAccountMaster getDormantAccountInformantion(DormantAccountMaster dormantAccountMasterObj) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT dam.id AS id, dam.account_number AS accountNumber, dam.account_opened_date AS accountOpenedDate,");
			queryBuilder.append("dam.account_balance AS accountBalance, dam.account_dormant_date AS accountDormantDate,dam.account_dormant_released_date AS accountDormantReleasedDate,");
			queryBuilder.append("dam.dormant_released_reason AS dormantReleasedReason, dam.released_by AS releasedBy ");
			queryBuilder.append("FROM dormant_account_master dam WHERE dam.account_number =   '"+dormantAccountMasterObj.getAccountNumber()+"'");
			
			List<DormantAccountMaster> dormantAccountMasters  = jdbcTemplate.query(queryBuilder.toString(), new BeanPropertyRowMapper<DormantAccountMaster>(DormantAccountMaster.class), new Object[]  {});
			if (dormantAccountMasters !=null && dormantAccountMasters.size() > 0) 
			{
				return dormantAccountMasters.get(0);
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
	public DormantAccountMaster getDormantStatusByAccountNumber(DormantAccountMaster dormantAccountMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();

			if (dormantAccountMaster.getAccountNumber() != null && dormantAccountMaster.getAccountNumber().trim().length() > 0) {
				criteria.add(Restrictions.eq("accountNumber", dormantAccountMaster.getAccountNumber()));
			}
			List<DormantAccountMaster> listData = (List<DormantAccountMaster>) criteria.list();
			if (listData != null && listData.size() > 0) {
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
	public int[] batchEntryOfDormantAccount(List<DormantAccountMaster> dormantAccountMaster) 
	{

		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO dormant_account_master "
					+ "(account_number, account_balance, account_opened_date) "
					+ "values(?,?,?)", new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, dormantAccountMaster.get(i).getAccountNumber());
							psmt.setString(2, dormantAccountMaster.get(i).getAccountBalance());
							psmt.setDate(3, Utils.getSqlDate(dormantAccountMaster.get(i).getAccountDormantDate()));						
						}
						
						@Override
						public int getBatchSize() 
						{
							return dormantAccountMaster.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeInfoLog("Exception in batchEntryOfInstanAccount::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	
	}

}
