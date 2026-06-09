package ams.cms.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.DormantAccountReqResStatusDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;

import ams.cms.model.DormantToActiveMaster;

@Repository
public class DormantAccountReqResStatusDaoImpl extends AbstractGenericDao<DormantToActiveMaster> implements DormantAccountReqResStatusDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(DormantAccountReqResStatusDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DormantToActiveMaster> getPendingCheckerUserList(DormantToActiveMaster dormantToActiveMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT request_date AS requestDate ,request_time AS requestTime, ");
			queryBuilder.append("account_number AS accountNumber , account_type AS accountType , ");
			queryBuilder.append("dormant_marked_date AS dormantMarkedDate , request_raised_by AS requestRaisedBy , ");
			queryBuilder.append("reason_for_active AS reasonForActive FROM dormant_to_active ");
			queryBuilder.append("WHERE status = 'Pending' ");
			
			List<DormantToActiveMaster> checkerPendingList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<DormantToActiveMaster>(DormantToActiveMaster.class), new Object[] {});
			
			if (checkerPendingList!=null && checkerPendingList.size() > 0) 
			{
				return checkerPendingList;
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
	public DormantToActiveMaster findDormantToActiveRequest(DormantToActiveMaster dormantToActiveMaster) 
	{
		try
		{
			Criteria criteria = createEntityCriteria();
			if (dormantToActiveMaster.getAccountNumber()!= null && dormantToActiveMaster.getAccountNumber().trim().length() > 0) 
			{
				criteria.add(Restrictions.eq("accountNumber", dormantToActiveMaster.getAccountNumber()));
			}

			List<DormantToActiveMaster> dormantToActiveMasters = (List<DormantToActiveMaster>) criteria.list();
			if (dormantToActiveMasters != null && dormantToActiveMasters.size() > 0) 
			{
				return dormantToActiveMasters.get(0);
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateDormantToActiveByAccountNumber(DormantToActiveMaster dormantToActiveMaster) 
	{
		String sqlUpdateQuery = "UPDATE dormant_to_active SET request_authorised_by = ? , request_authorised_date = ? , request_authorised_time = ?, request_authorised_reason = ?, status = ?  WHERE account_number = ? ";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] 
			{
				dormantToActiveMaster.getRequestAuthorisedBy(),
				dormantToActiveMaster.getRequestAuthorisedDate(),
				dormantToActiveMaster.getRequestAuthorisedTime(), 
				dormantToActiveMaster.getRequestAuthorisedReason(),
				dormantToActiveMaster.getStatus(),
				dormantToActiveMaster.getAccountNumber()
			});
			
			amsLogger.writeInfoLog(sqlUpdateQuery);
			return count;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	@Override
	public int updateDormantToActiveByAccountNumberForReject(DormantToActiveMaster dormantToActiveMaster) 
	{
		String sqlUpdateQuery = "UPDATE dormant_to_active SET request_rejected_by = ? , request_rejected_date = ? , request_rejected_time = ?, request_rejected_reason = ?, status = ? WHERE account_number = ? ";
		try 
		{
			int count = this.jdbcTemplate.update(sqlUpdateQuery, new Object[] 
			{
				dormantToActiveMaster.getRequestRejectedBy(),
				dormantToActiveMaster.getRequestRejectedDate(),
				dormantToActiveMaster.getRequestRejectedTime(),
				dormantToActiveMaster.getRequestAuthorisedReason(),
				dormantToActiveMaster.getStatus(),
				dormantToActiveMaster.getAccountNumber()
			});
			
			amsLogger.writeInfoLog(sqlUpdateQuery);
			return count;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

}
