package ams.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.PullAccountDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.PullAccountModel;



@Repository
public class PullAccountDaoImpl extends AbstractGenericDao<PullAccountModel> implements PullAccountDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(PullAccountDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int updateSendAccountMoneyList(AccountTranMaster accountTranMaster) 
	{
		int count = 0;
		try
		{
			List<Object> objectList = new ArrayList<Object>();
			
			StringBuilder queryBuilder = new StringBuilder("UPDATE account_tran_master SET ");
			
			if (accountTranMaster.getStrReservefield1() != null && accountTranMaster.getStrReservefield1().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field1 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield1());
			}
			if (accountTranMaster.getStrReservefield2()!=null && accountTranMaster.getStrReservefield2().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field2 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield2());
			}	
			if (accountTranMaster.getStrReservefield3()!=null && accountTranMaster.getStrReservefield3().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field3 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield3());
			}	
			if (accountTranMaster.getStrReservefield4()!=null && accountTranMaster.getStrReservefield4().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field4 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield4());
			}
			if (accountTranMaster.getStrReservefield5()!=null && accountTranMaster.getStrReservefield5().trim().length() > 0)
			{
				if (queryBuilder.toString().indexOf("?") != -1)
				{
					queryBuilder.append(",");
				}
				queryBuilder.append("reserve_field5 = ").append("?");
				objectList.add(accountTranMaster.getStrReservefield5());
			}
			
			queryBuilder.append(" WHERE txn_id = ").append("?");
			objectList.add(accountTranMaster.getStrTxn_id());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(queryBuilder.toString(), object);
			return count;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PullAccountModel getPullAccountModelByParticipantId(PullAccountModel pullAccountModel) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (pullAccountModel.getStrParticipantId()!=null && pullAccountModel.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", pullAccountModel.getStrParticipantId().trim()));
				
				List<PullAccountModel> listData = (List<PullAccountModel>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}
				else 
				{
					amsLogger.writeInfoLog("---------- No Pull Account Data Found ----------");
				}
			}
			else 
			{
				amsLogger.writeInfoLog("---------- No Participant Found ----------");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public PullAccountModel getPullAccountModelByBankCode(PullAccountModel pullAccountModel) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (pullAccountModel.getStrPoolAccountBankCode()!=null)
			{
				criteria.add(Restrictions.eq("strPoolAccountBankCode", pullAccountModel.getStrPoolAccountBankCode().trim()));
				
				@SuppressWarnings("unchecked")
				List<PullAccountModel> listData = (List<PullAccountModel>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}				
			}
			else 
			{
				amsLogger.writeInfoLog("---------- No Pool Account  Found ----------");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
