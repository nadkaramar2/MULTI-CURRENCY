package ams.cms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.model.AccountMaster;
import ams.cms.dao.MultiCurrencyFinancialYearMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.MultiCurrencyFinancialYearMaster;

@Repository
public class MultiCurrencyFinancialYearMasterDaoImpl extends AbstractGenericDao<MultiCurrencyFinancialYearMaster> implements MultiCurrencyFinancialYearMasterDao{

	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public MultiCurrencyFinancialYearMaster getFinancialYearNyAccountNumber(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT fy.account_number AS accountNumber , fy.channel_code AS channelCode  , fy.total_lrs_consumed as totalLrsConsumed ,  fy.cust_id AS custId , fy.total_tcs_on as totalTcsOn , fy.lrs_limit as lrsLimit , fy.available_lrs_limit as availableLrsLimit , fy.total_excess_loading as totalExcessLoading , fy.total_loaded as totalLoaded ,   fy.account_type AS accountType , fy.financial_year AS financialYear ");
			if(multiCurrencyFinancialYearMaster.getChannelCode() != null) {
				queryBuilder.append(" FROM multi_currency_financial_year_master fy WHERE fy.account_number= '"+multiCurrencyFinancialYearMaster.getAccountNumber()+"' AND fy.financial_year = '"+multiCurrencyFinancialYearMaster.getFinancialYear()+"'  AND fy.account_type = '"+multiCurrencyFinancialYearMaster.getAccountType()+"'   AND fy.channel_code = '"+multiCurrencyFinancialYearMaster.getChannelCode()+"'");
					
			}else {
				queryBuilder.append(" FROM multi_currency_financial_year_master fy WHERE fy.account_number= '"+multiCurrencyFinancialYearMaster.getAccountNumber()+"' AND fy.financial_year = '"+multiCurrencyFinancialYearMaster.getFinancialYear()+"'  AND fy.account_type = '"+multiCurrencyFinancialYearMaster.getAccountType()+"'");
				
			}
			
			List<MultiCurrencyFinancialYearMaster> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyFinancialYearMaster>(MultiCurrencyFinancialYearMaster.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

	@Override
	public void updatFinancialYearTable(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		try
		{
			StringBuilder updateQueryBuilder = new StringBuilder("UPDATE multi_currency_financial_year_master fy  ");
			updateQueryBuilder.append( "SET fy.total_lrs_consumed = '"+multiCurrencyFinancialYearMaster.getTotalLrsConsumed()+"' , ");
			updateQueryBuilder.append( " fy.total_tcs_on = '"+multiCurrencyFinancialYearMaster.getTotalTcsOn()+"' , ");
			updateQueryBuilder.append( " fy.lrs_limit = '"+multiCurrencyFinancialYearMaster.getLrsLimit()+"' ,  ");
			updateQueryBuilder.append( " fy.available_lrs_limit = '"+multiCurrencyFinancialYearMaster.getAvailableLrsLimit()+"' ,  ");
			updateQueryBuilder.append( " fy.total_excess_loading = '"+multiCurrencyFinancialYearMaster.getTotalExcessLoading()+"' ,  ");
			updateQueryBuilder.append( " fy.total_loaded = '"+multiCurrencyFinancialYearMaster.getTotalLoaded()+"' ");
			
			updateQueryBuilder.append(" Where  fy.account_number = '"+multiCurrencyFinancialYearMaster.getAccountNumber()+"'");
			updateQueryBuilder.append(" AND fy.account_type = '"+multiCurrencyFinancialYearMaster.getAccountType()+"'");
			updateQueryBuilder.append(" AND fy.financial_year = '"+multiCurrencyFinancialYearMaster.getFinancialYear()+"'");
			updateQueryBuilder.append(" AND fy.channel_code = '"+multiCurrencyFinancialYearMaster.getChannelCode()+"'");
			
			
			int update = this.jdbcTemplate.update(updateQueryBuilder.toString(), new Object[] {});
			
		}
		catch (Exception e) 
		{
			
		}
		
	}

	
	

	@Override
	public List<AccountMaster> resetMultiCurrencyFinancialYearMaster(
			MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MultiCurrencyFinancialYearMaster> getListFinancialYearNyAccountNumberAndChannel(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMasterObj) {
		
		List<MultiCurrencyFinancialYearMaster> multiCurrencyFinancialYearMasters = new ArrayList<>();
		
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT fy.account_number AS accountNumber , fy.total_lrs_consumed as totalLrsConsumed , fy.channel_code as channelCode ,  fy.cust_id AS custId , fy.total_tcs_on as totalTcsOn , fy.lrs_limit as lrsLimit , fy.available_lrs_limit as availableLrsLimit , fy.total_excess_loading as totalExcessLoading , fy.total_loaded as totalLoaded ,   fy.account_type AS accountType , fy.financial_year AS financialYear ");
			queryBuilder.append(" FROM multi_currency_financial_year_master fy WHERE fy.account_number= '"+multiCurrencyFinancialYearMasterObj.getAccountNumber()+"' AND fy.financial_year = '"+multiCurrencyFinancialYearMasterObj.getFinancialYear()+"' AND fy.channel_code = '"+multiCurrencyFinancialYearMasterObj.getChannelCode()+"' AND fy.account_type = '"+multiCurrencyFinancialYearMasterObj.getAccountType()+"'");
			
			
			multiCurrencyFinancialYearMasters  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<MultiCurrencyFinancialYearMaster>(MultiCurrencyFinancialYearMaster.class),
			new Object[]  {});
			
			if (multiCurrencyFinancialYearMasters!=null && multiCurrencyFinancialYearMasters.size() > 0) 
			{
				return multiCurrencyFinancialYearMasters;
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return multiCurrencyFinancialYearMasters;
}

}
