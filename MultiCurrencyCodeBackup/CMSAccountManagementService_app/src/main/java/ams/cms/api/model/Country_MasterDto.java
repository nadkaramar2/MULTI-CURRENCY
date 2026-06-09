package ams.cms.api.model;

import java.io.Serializable;


public class Country_MasterDto implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String countryId;
	private String countryName;
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	
	
}
