package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.CountryCodeMasterDao;
import ams.cms.api.model.CityModel;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.StateModel;
import ams.cms.api.model.State_MasterDto;
import ams.cms.api.service.CountryCodeMasterService;

@Transactional
@Service
public class CountryCodeMasterServiceImpl implements CountryCodeMasterService
{
	@Autowired
	CountryCodeMasterDao countryCodeMasterDao;

	@Override
	public List<CountryCodeMaster> getCountryCode(CountryCodeMaster countryCodeMaster) {
		return countryCodeMasterDao.getCountryCode(countryCodeMaster);
	}

	@Override
	public List<CountryCodeMaster> getPhoneCode() throws Exception {
		return countryCodeMasterDao.getPhoneCode();
	}
	
	@Override
	public List<Country_MasterDto> getCountrylist(Country_MasterDto countryMaster) throws Exception 
	{
		 return countryCodeMasterDao.getCountrylist(countryMaster);
	}

	@Override
	public List<State_MasterDto> getStatelist(StateModel stateMaster) throws Exception 
	{
		return countryCodeMasterDao.getStatelist(stateMaster);
	}

	@Override
	public List<City_MasterDto> getCitylist(CityModel cityMaster) 
	{
		return countryCodeMasterDao.getCitylist(cityMaster);
	}


}
