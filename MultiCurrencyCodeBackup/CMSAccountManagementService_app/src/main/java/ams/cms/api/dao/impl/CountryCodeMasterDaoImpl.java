package ams.cms.api.dao.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.CountryCodeMasterDao;
import ams.cms.api.model.CityModel;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.StateModel;
import ams.cms.api.model.State_MasterDto;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;


@Repository
public class CountryCodeMasterDaoImpl extends AbstractGenericDao<CountryCodeMaster> implements CountryCodeMasterDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CountryCodeMasterDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	//added by prashant Tayde for country code api

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<CountryCodeMaster> getCountryCode(CountryCodeMaster countryCodeMaster) 
	{
		try 
		{
			String sql = "SELECT CONCAT('+',phonecode,'-',country_name) AS country_code, phonecode AS phonecode FROM country_code_master";
			
			List<CountryCodeMaster> countryCodeDetails = this.jdbcTemplate.query(sql,
			  (RowMapper) new BeanPropertyRowMapper(CountryCodeMaster.class), new Object[]{});
			System.out.println("CountryCodeMasterDaoImpl.getCountryCode()"+countryCodeDetails);
			return countryCodeDetails;
	}
	catch(Exception e) {
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
		return null;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Override
	public List<CountryCodeMaster> getPhoneCode()
	{
		try
		{
			List<CountryCodeMaster> phoneCodeList = this.jdbcTemplate.query("SELECT shortname AS shortname, phonecode AS strPhoneCode FROM country_code_master", 
					(RowMapper) new BeanPropertyRowMapper(CountryCodeMaster.class), new Object[0]);
			System.out.println("ConfigurationDaoImpl.getPhoneCode()" + phoneCodeList);
			return phoneCodeList;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	@Override
	public List<Country_MasterDto> getCountrylist(Country_MasterDto countryMaster) 
	{
		try 
		{
			String sql = "SELECT ccm.id as countryId,ccm.country_name as countryName  FROM  country_code_master as ccm";
			
			List<Country_MasterDto> countryMasterlist = this.jdbcTemplate.query(sql,
			  (RowMapper) new BeanPropertyRowMapper(Country_MasterDto.class), new Object[]{});
			System.out.println("CountryCodeMasterDaoImpl.getCountryCode()"+countryMasterlist);
			return countryMasterlist;
	}
	catch(Exception e) {
		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
	}
		return null;
 }
    
	@SuppressWarnings({"rawtypes", "unchecked","deprecation" })
	@Override
	public List<State_MasterDto> getStatelist(StateModel stateMaster) 
	{
		  try
		  {
				String sql = "SELECT id as stateId,state_name as stateName FROM state_master WHERE country_id='"+stateMaster.getStrCountryID()+"' ";
				System.out.println(sql);
				List<State_MasterDto> stateCodeList = this.jdbcTemplate.query(sql,
				  (RowMapper) new BeanPropertyRowMapper(State_MasterDto.class), new Object[]{});
				System.out.println("CountryCodeMasterDaoImpl.getCountryCode()"+stateCodeList);
				return stateCodeList;
		  }
		 catch (Exception e) 
		  {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		return null;
	}
 

	
	
	
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	@Override
	public List<City_MasterDto> getCitylist(CityModel cityMaster) 
	{
		try
		  {
			String sql = "SELECT cm.id as cityId ,cm.city_name as cityName from city_master AS cm WHERE cm.state_id='"+cityMaster.getStrStateID()+"'";
			System.out.println(sql);
			List<City_MasterDto> cityCodeList = this.jdbcTemplate.query(sql,
			  (RowMapper) new BeanPropertyRowMapper(City_MasterDto.class), new Object[]{});
			System.out.println("CountryCodeMasterDaoImpl.getCountryCode()"+cityCodeList);
			return cityCodeList;
			
		  }
		 catch (Exception e) 
		  {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		 }
		return null;
	}
}
