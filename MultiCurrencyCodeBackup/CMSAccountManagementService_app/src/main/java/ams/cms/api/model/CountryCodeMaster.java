package ams.cms.api.model;

import java.io.Serializable;

public class CountryCodeMaster implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String country_code;
	private String shortname;
	private String phonecode;
	private String country_name;
	
	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getPhonecode() {
		return phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
}
