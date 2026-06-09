package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.TaxConfigModel;
import ams.cms.services.TaxConfigModelService;

@RestController
@RequestMapping("/tax_type_config")
public class TaxConfigModelController 
{
	@Autowired
	TaxConfigModelService taxConfigModelService;
	
	@RequestMapping(value = "/getTaxTypeConfig", method = RequestMethod.POST)
	public ResponseEntity<?> getTaxTypeConfigModelsList(@RequestBody TaxConfigModel taxConfigModel)
	{
		try 
		{
			List<TaxConfigModel> categoryListModelsList = taxConfigModelService.getTaxTypeConfigModelsList(taxConfigModel);
			return ResponseEntity.ok(categoryListModelsList);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
