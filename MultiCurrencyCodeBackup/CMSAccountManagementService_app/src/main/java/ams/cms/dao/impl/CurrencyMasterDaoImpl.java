package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CurrencyMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CurrencyMaster;
import ams.cms.util.ProcessResponse;

@Repository
public class CurrencyMasterDaoImpl extends AbstractGenericDao<CurrencyMaster> implements CurrencyMasterDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public CurrencyMaster getCurrencyMasterByCurrencyCode(CurrencyMaster currencyMaster) {
		try 
		{	
			StringBuilder sql = new StringBuilder(" SELECT cm.currency_code AS currencyCode , cm.gl_account_type AS glAccountType , cm.gl_account_number AS glAccountNumber , cm.fee_type AS feeType , ");
			sql.append( " cm.tcs_type AS tcsType , cm.base_country as baseCountry  FROM currency_master cm WHERE cm.currency_code= '"+currencyMaster.getCurrencyCode().trim()+"' ");
			
			

			List<CurrencyMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<CurrencyMaster>(CurrencyMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<CurrencyMaster> getCurrencyList() {
		try 
		{	
			StringBuilder sql = new StringBuilder(" SELECT * from currency_master ");
		
			

			List<CurrencyMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<CurrencyMaster>(CurrencyMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters;
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CurrencyMaster getCurrencyMasterByCurrencyCode() {
		try 
		{	
			StringBuilder sql = new StringBuilder(" SELECT cm.currency_code AS currencyCode , cm.gl_account_type AS glAccountType , cm.gl_account_number AS glAccountNumber , cm.fee_type AS feeType , ");
			sql.append( " cm.tcs_type AS tcsType , cm.base_country as baseCountry  FROM currency_master cm WHERE cm.base_country = 'Y' ");
			
			

			List<CurrencyMaster> accountMasters  = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<CurrencyMaster>(CurrencyMaster.class), new Object[]  {});
			if (accountMasters!=null && accountMasters.size() > 0) 
			{
				return accountMasters.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CurrencyMaster getCurrencyDetailByCurrencyCode(CurrencyMaster currencyMaster) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT cm.currency_code AS currencyCode,cm.base_country AS baseCountry,cm.gl_account_type AS glAccountType,cm.gl_account_number AS glAccountNumber, ");
			queryBuilder.append("cm.fee_type AS feeGlAccountType,mcft.fee_amt AS feeAmount,mcft.gl_account_type AS feeGl,mcft.gl_account_number AS feeGLAccountNumber, ");
			queryBuilder.append("gtm.gst_type AS gstType,gtm.gst_percentage AS gstPercentage,gtm.gl_account_type as gstGL,gtm.gl_account_number AS gstAccountNumber, ");
			queryBuilder.append("mcft.is_flat_fee AS strFlatFee, mcft.percentage_fee AS strFeePercentage, mcft.is_percentage_fee AS strIsFee ");
			queryBuilder.append("FROM currency_master cm INNER JOIN multi_currency_fee_type_master mcft ");
			queryBuilder.append("ON cm.fee_type = mcft.fee_type INNER JOIN gst_type_master gtm ");
			queryBuilder.append("ON mcft.gst_type = gtm.gst_type WHERE cm.currency_code = ?");
			
			List<CurrencyMaster> currencyList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<CurrencyMaster>(CurrencyMaster.class), new Object[]  
			{ 
					currencyMaster.getCurrencyCode()
			});
			if (currencyList!=null && currencyList.size() > 0) 
			{
				return currencyList.get(0);
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
