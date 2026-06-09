package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.TierAccountMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TierAccountMaster;

@Repository
public class TierAccountMasterDaoImpl extends AbstractGenericDao<TierAccountMaster> implements TierAccountMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TierAccountMasterDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public TierAccountMaster getTierBasedLimitsValues(TierAccountMaster TierAccountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT tam.tier1_daily_cum_limit as tier1DailyLimiy, tam.tier2_daily_cum_limit as tier2DailyLimiy, ");
			queryBuilder.append("tam.tier3_daily_cum_limit as tier3DailyLimiy, cm.active_tier as strActiveTier, ");
			queryBuilder.append("available_tier1_daily_cum_limit as AvailblTier1DailyLimiy, available_tier2_daily_cum_limit as AvailblTier2DailyLimiy, available_tier3_daily_cum_limit as AvailblTier3DailyLimiy, ");
			queryBuilder.append("tam.tier1_cummulative_balance as tier1CummBalance, tam.tier2_cummulative_balance as tier2CummBalance, tam.tier3_cummulative_balance as tier3CummBalance");
			queryBuilder.append("from tier_account_master as tam ");
			queryBuilder.append("inner join customer_master cm on tam.cust_id = cm.cust_id where tam.account_no = ? ");
			queryBuilder.append("AND tam.account_type = ? ");
			List<TierAccountMaster> accountCretion  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<TierAccountMaster>(TierAccountMaster.class),
			     new Object[]  { 
			    		 TierAccountMaster.getStrAccountNo(), 
			    		});
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
	public int updateCummAvailableBalance(TierAccountMaster TierAccountMaster)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master SET ");
			
			if ("tier1".equalsIgnoreCase(TierAccountMaster.getStrActiveTier()))
			{
				queryBuilder.append("available_tier1_daily_cum_limit = '"+TierAccountMaster.getStrAvailableTier1DailyCumlimit()+"'");
			}
			else if ("tier2".equalsIgnoreCase(TierAccountMaster.getStrActiveTier())) 
			{
				queryBuilder.append("available_tier2_daily_cum_limit = '"+TierAccountMaster.getStrAvailableTier2DailyCumlimit()+"'");
			}
			else if ("tier3".equalsIgnoreCase(TierAccountMaster.getStrActiveTier())) 
			{
				queryBuilder.append("available_tier3_daily_cum_limit = '"+TierAccountMaster.getStrAvailableTier3DailyCumlimit()+"'");
			}
			queryBuilder.append("where account_no = '"+TierAccountMaster.getStrAccountNo().trim()+"' AND account_type = '"+TierAccountMaster.getStrAccountType()+"'");
			
			amsLogger.writeInfoLog("Inside updateCummAvailableBalance Query:: "+queryBuilder.toString());
			
			int count = this.jdbcTemplate.update(queryBuilder.toString(), new Object[] {});			
			amsLogger.writeInfoLog("updateCummAvailableBalance count::["+count+"]");
			
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public TierAccountMaster getTierInfoByCustID(TierAccountMaster tierAccountMaster) 
	{
		try 
		{
			StringBuilder querySb = new StringBuilder("SELECT tam.cust_id AS strCustId, tam.account_no AS strAccountNo, tam.account_type AS strAccountType,");
			querySb.append("cm.active_tier as strActiveTier, cm.tier1_status as strTier1Status,");
			querySb.append("cm.tier2_status as strTier2Status, cm.tier3_status as strTier3Status, ");
			querySb.append("tam.tier1_daily_cum_limit AS strTier1DailyCumlimit, ");
			querySb.append("tam.tier2_daily_cum_limit AS strTier2DailyCumlimit, ");
			querySb.append("tam.tier3_daily_cum_limit AS strTier3DailyCumlimit,");
			querySb.append("tam.available_tier1_daily_cum_limit AS strAvailableTier1DailyCumlimit, ");
			querySb.append("tam.available_tier2_daily_cum_limit AS strAvailableTier2DailyCumlimit,");
			querySb.append("tam.available_tier3_daily_cum_limit AS strAvailableTier3DailyCumlimit,");
			querySb.append("attbl.cumulative_balance_limit AS strCumulativeBalanceLimit ");
			querySb.append("FROM customer_master cm INNER JOIN tier_account_master tam ON tam.cust_id = cm.cust_id ");
			querySb.append("INNER JOIN account_type_tier_based_limit attbl ON attbl.account_type = tam.account_type ");
			querySb.append("WHERE tam.account_no = ? AND attbl.tier_type = cm.active_tier");
			
			List<TierAccountMaster> tierAccountMasters  = jdbcTemplate.query(querySb.toString(),
			new BeanPropertyRowMapper<TierAccountMaster>(TierAccountMaster.class), new Object[] { tierAccountMaster.getStrAccountNo()});
			if (tierAccountMasters!=null && tierAccountMasters.size() > 0) 
			{
				return tierAccountMasters.get(0);
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int[] updatesBatchEntryOfTier1LimitsAccountMasterFields(List<TierAccountMaster> tier1AccountMasterlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master SET available_tier1_daily_cum_limit = ? ");
			queryBuilder.append("where account_no = ? AND account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(queryBuilder.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, String.valueOf(tier1AccountMasterlist.get(i).getStrAvailableTier1DailyCumlimit()));
							psmt.setString(2, tier1AccountMasterlist.get(i).getStrAccountNo());
							psmt.setString(3, tier1AccountMasterlist.get(i).getStrAccountType());							
						}						
						@Override
						public int getBatchSize() {
							return tier1AccountMasterlist.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] updatesBatchEntryOfTier2LimitsAccountMasterFields(List<TierAccountMaster> tier2AccountMasterlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master SET available_tier2_daily_cum_limit = ? ");
			queryBuilder.append("where account_no = ? AND account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(queryBuilder.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, String.valueOf(tier2AccountMasterlist.get(i).getStrAvailableTier2DailyCumlimit()));
							psmt.setString(2, tier2AccountMasterlist.get(i).getStrAccountNo());
							psmt.setString(3, tier2AccountMasterlist.get(i).getStrAccountType());							
						}						
						@Override
						public int getBatchSize() {
							return tier2AccountMasterlist.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int[] updatesBatchEntryOfTier3LimitsAccountMasterFields(List<TierAccountMaster> tier3AccountMasterlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master SET available_tier3_daily_cum_limit = ? ");
			queryBuilder.append("where account_no = ? AND account_type = ?");
			
			return this.jdbcTemplate.batchUpdate(queryBuilder.toString(), new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, String.valueOf(tier3AccountMasterlist.get(i).getStrAvailableTier3DailyCumlimit()));
							psmt.setString(2, tier3AccountMasterlist.get(i).getStrAccountNo());
							psmt.setString(3, tier3AccountMasterlist.get(i).getStrAccountType());							
						}						
						@Override
						public int getBatchSize() {
							return tier3AccountMasterlist.size();
						}
					});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@Override
	public int updateCummulativeDailyLimitBasedOnActiveTier(TierAccountMaster tierAccountMaster) 
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master SET ");
			queryBuilder.append(tierAccountMaster.getStrUpdatableDailyAvailableLimitQuery()).append(" ");
			
			queryBuilder.append("where account_no = '"+tierAccountMaster.getStrAccountNo().trim()+"' ");
			if (tierAccountMaster.getStrCustId()!=null && tierAccountMaster.getStrCustId().trim().length() > 0) 
			{
				queryBuilder.append("AND cust_id = '"+tierAccountMaster.getStrCustId().trim()+"' ");
			}
			if (tierAccountMaster.getStrAccountType()!=null && tierAccountMaster.getStrAccountType().trim().length() > 0)
			{
				queryBuilder.append( "AND account_type = '"+tierAccountMaster.getStrAccountType().trim()+"' ");
			}
			amsLogger.writeInfoLog("Inside updateCummulativeDailyLimitBasedOnActiveTier Query:: "+queryBuilder.toString());
			
			int count = this.jdbcTemplate.update(queryBuilder.toString(), new Object[] {});			
			amsLogger.writeInfoLog("updateCummulativeDailyLimitBasedOnActiveTier count::["+count+"]");
			
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int[] updatesBatchEntryOfTierDailyLimitsAccountMasterFields(List<TierAccountMaster> tierAccountMasterlist) 
	{
		int[] batchResponse = null;
		try
		{
			StringBuilder queryBuilder = new StringBuilder("UPDATE tier_account_master AS tam SET tam.available_tier1_daily_cum_limit = ?, tam.available_tier2_daily_cum_limit = ?,");
			queryBuilder.append("tam.available_tier3_daily_cum_limit = ? WHERE tam.cust_id = ? AND tam.account_no = ? ");
			
			return this.jdbcTemplate.batchUpdate(queryBuilder.toString(), new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, String.valueOf(tierAccountMasterlist.get(i).getStrTier1DailyCumlimit()));
					psmt.setString(2, String.valueOf(tierAccountMasterlist.get(i).getStrTier2DailyCumlimit()));
					psmt.setString(3, String.valueOf(tierAccountMasterlist.get(i).getStrTier3DailyCumlimit()));
					psmt.setString(4, tierAccountMasterlist.get(i).getStrCustId());
					psmt.setString(5, tierAccountMasterlist.get(i).getStrAccountNo());							
				}						
				@Override
				public int getBatchSize() 
				{
					return tierAccountMasterlist.size();
				}
			});
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return batchResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TierAccountMaster> getTierAccountMasterDailyLimits() 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			List<TierAccountMaster> listData = (List<TierAccountMaster>) criteria.list();
			if (listData != null && listData.size() > 0) 
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

}
