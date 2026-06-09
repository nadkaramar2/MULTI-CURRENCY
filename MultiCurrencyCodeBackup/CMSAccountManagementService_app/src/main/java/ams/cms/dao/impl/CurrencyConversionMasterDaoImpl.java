package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CurrencyConversionMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;

@Repository
public class CurrencyConversionMasterDaoImpl  extends AbstractGenericDao<CurrencyConversionMaster> implements CurrencyConversionMasterDao {

	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public CurrencyConversionMaster getCurrencyConverionValue(CurrencyConversionMaster currencyConversionMaster) {
		
	try 
	{
			StringBuilder queryBuilder = new StringBuilder("SELECT ccm.to_currency_conversion AS fromCurrencyValue , ccm.to_currency_conversion AS toCurrencyConversion");
			queryBuilder.append(" FROM currency_conversion_master ccm WHERE ccm.from_currency = '"+currencyConversionMaster.getFromCurrency()+"' AND ccm.to_currency= '"+currencyConversionMaster.getToCurrency()+"'");
			
			
			List<CurrencyConversionMaster> accountCreations  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<CurrencyConversionMaster>(CurrencyConversionMaster.class),
			new Object[]  {});
			
			if (accountCreations!=null && accountCreations.size() > 0) 
			{
				return accountCreations.get(0);
			}	
		} 
	catch (Exception e) 
	{
		
	}

	return null;
	}

	@Override
	public CurrencyConversionMaster getCurrencyMasterByCurrencyCode(CurrencyConversionMaster currencyConversionMaster)
	{
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT ccm.from_currency AS fromCurrency,ccm.to_currency AS toCurrency,ccm.to_currency_conversion AS toCurrencyConversion, ");
			queryBuilder.append("ccm.from_currency_value AS fromCurrencyValue FROM currency_conversion_master ccm WHERE  ");
			queryBuilder.append("ccm.from_currency = ? AND ccm.to_currency = ? ");
			
			List<CurrencyConversionMaster> currencyConversionMasterList  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<CurrencyConversionMaster>(CurrencyConversionMaster.class), new Object[]  
			{ 
				currencyConversionMaster.getFromCurrency(),
				currencyConversionMaster.getToCurrency()
			});
			System.out.println("queryBuilder.toString()"+queryBuilder.toString());	
			if (currencyConversionMasterList != null && currencyConversionMasterList.size() > 0) 
			{
				return currencyConversionMasterList.get(0);
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
