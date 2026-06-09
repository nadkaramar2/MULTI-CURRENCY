package ams.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.CustomerIdTable;
import ams.cms.services.CustomerIdTableService;

@RestController
@RequestMapping("/customerIdTable")
public class CustomerIdTableController {
	
	@Autowired
	CustomerIdTableService customerIdTableService;
	
	@RequestMapping(value = "/updateCustId", method = RequestMethod.POST)
	public ResponseEntity<?> insertCustId(@RequestBody CustomerIdTable customerIdTable)
	{
		Integer count = 0;
		try 
		{
			 count = customerIdTableService.updateCustIdTable(customerIdTable);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(count);
	}

}
