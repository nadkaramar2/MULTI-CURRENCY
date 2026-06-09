package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.service.CountryCodeMasterService;

@RestController
@RequestMapping("/country_code_api")
public class CountryCodeMasterController 
{
	@Autowired
	CountryCodeMasterService countryCodeMasterService;
	
	@RequestMapping(value = "/getCountryCode", method = RequestMethod.POST)
	public ResponseEntity<?> processToGetAccountTypeList(@RequestBody CountryCodeMaster countryCodeMaster)
	{
		try 
		{
			List<CountryCodeMaster> listData = countryCodeMasterService.getCountryCode(countryCodeMaster);
			return ResponseEntity.ok(listData);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	
}
