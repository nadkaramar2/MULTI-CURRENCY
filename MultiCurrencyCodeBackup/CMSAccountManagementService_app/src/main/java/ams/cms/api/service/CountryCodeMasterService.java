package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.CityModel;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.StateModel;
import ams.cms.api.model.State_MasterDto;

public interface CountryCodeMasterService 
{
	List<CountryCodeMaster> getCountryCode(CountryCodeMaster countryCodeMaster);
	
	List<CountryCodeMaster> getPhoneCode() throws Exception;
	
	List<Country_MasterDto> getCountrylist(Country_MasterDto countryMaster) throws Exception;

	//List<State_MasterDto> getStatelist(State_MasterDto stateMaster) throws Exception;

	List<City_MasterDto> getCitylist(CityModel cityMaster);

	List<State_MasterDto> getStatelist(StateModel stateMaster) throws Exception;
}
