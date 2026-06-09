package ams.cms.dao.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.RevolvingCreditCardDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.RevolvingCreditCardMaster;

@Repository
public class RevolvingCreditCardDaoImpl extends AbstractGenericDao<RevolvingCreditCardMaster> implements RevolvingCreditCardDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(RevolvingCreditCardDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public String getGracePeriod(String accountType) 
	{
		try 
		{
			String gracePeriod = this.jdbcTemplate.queryForObject("select grace_period_in_days from revolving_credit_card_master where account_type = ?",String.class,
					new Object[] { accountType });
			amsLogger.writeInfoLog("gracePeriod::" + gracePeriod);
			return gracePeriod;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getRemainingGracePeriodInDays(RevolvingCreditCardMaster revolvingCreditCardMaster)
	{
		try 
		{
			StringBuilder selectQuerySb = new StringBuilder("SELECT DATEDIFF((date_format(CAST(CURRENT_TIMESTAMP as DATE),'%Y-"+revolvingCreditCardMaster.getStrBillingMonthAndDay()+"')), CAST(CURRENT_TIMESTAMP as DATE)) + "+revolvingCreditCardMaster.getStrGracePeriodInDays()+" AS strRemainingGracePeriod");
			
			String remainingGracePeriodInDays = this.jdbcTemplate.queryForObject(selectQuerySb.toString(), String.class, new Object[] {});
			amsLogger.writeInfoLog("remainingGracePeriodInDays::" + remainingGracePeriodInDays);
			return remainingGracePeriodInDays;
			
			/*
			StringBuilder selectQuerSb = new StringBuilder("SELECT ROUND(am.total_available_grace_period - ( CAST(CURRENT_TIMESTAMP as DATE) - am.grace_period_start_date )) AS strRemainingGracePeriod ");
			selectQuerSb.append("FROM account_master am INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			selectQuerSb.append("WHERE am.account_number = ?");
			String remainingGracePeriodInDays = this.jdbcTemplate.queryForObject(selectQuerySb.toString(), String.class, new Object[] { revolvingCreditCardMaster.getStrAccounNumber() });
			 */
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/*
	@Override
	public String getRemainingGracePeriodInDays(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster)
	{
		try 
		{
			StringBuilder selectQuerSb = new StringBuilder("SELECT ROUND(am.total_available_grace_period - ( CAST(CURRENT_TIMESTAMP as DATE) - am.grace_period_start_date )) AS strRemainingGracePeriod ");
		
			selectQuerSb.append("FROM account_master am INNER JOIN account_type_master atm ON am.account_type = atm.account_type ");
			selectQuerSb.append("WHERE am.account_number = ?");
			
			String remainingGracePeriodInDays = this.jdbcTemplate.queryForObject(selectQuerSb.toString(), String.class, new Object[] { revolvingCreditCardTxnMaster.getStrAccountNumber() });
			amsLogger.writeInfoLog("remainingGracePeriodInDays::" + remainingGracePeriodInDays);
			return remainingGracePeriodInDays;
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	*/

}
