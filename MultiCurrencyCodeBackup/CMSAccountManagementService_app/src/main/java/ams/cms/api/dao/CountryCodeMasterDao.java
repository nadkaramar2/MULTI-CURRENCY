package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.CityModel;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.StateModel;
import ams.cms.api.model.State_MasterDto;
import ams.cms.dao.GenericDao;

public interface CountryCodeMasterDao extends GenericDao<CountryCodeMaster>
{
	List<CountryCodeMaster> getCountryCode(CountryCodeMaster countryCodeMaster);
	
	List<CountryCodeMaster> getPhoneCode();
	
	List<Country_MasterDto> getCountrylist(Country_MasterDto countryMaster);

	List<State_MasterDto> getStatelist(StateModel stateMaster);

	List<City_MasterDto> getCitylist(CityModel cityMaster);
}
