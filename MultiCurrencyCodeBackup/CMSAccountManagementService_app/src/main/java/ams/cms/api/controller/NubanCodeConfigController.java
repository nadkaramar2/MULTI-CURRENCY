package ams.cms.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.NubanCodeConfig;
import ams.cms.api.service.NubanCodeConfigService;
import ams.cms.services.AccountTypeMasterService;



@RestController
@RequestMapping(value = "/nubanCode")
public class NubanCodeConfigController {

	@Autowired
	private NubanCodeConfigService nubanCodeConfigService;
	
	@Autowired
	AccountTypeMasterService  accountTypeMasterService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addNubanCodeConfig(@RequestBody NubanCodeConfig nubanCodeConfig){
		int addNubanCodeConfig = nubanCodeConfigService.addNubanCodeConfig(nubanCodeConfig);
//		if(addNubanCodeConfig>0) {
//			return new ResponseEntity<>("Nuban Configured",HttpStatus.OK);
//		}
//		//nubanCodeConfigService.addNewNubanCodeConfig(nubanCodeConfig);
//		return new ResponseEntity<>("Nuban Code already Configured",HttpStatus.OK);
		return new ResponseEntity<>(addNubanCodeConfig,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public ResponseEntity<?> getNubanCodeConfig(@RequestBody NubanCodeConfig nubanCondeConfig){
		List<NubanCodeConfig> nubanCodeConfig = nubanCodeConfigService.getNubanCodeConfig(nubanCondeConfig);
		return ResponseEntity.ok(nubanCodeConfig);
	}
	
	@RequestMapping(value = "/getNubanCode", method = RequestMethod.POST)
	public ResponseEntity<?> getNubanCodeConfigMaster(@RequestBody NubanCodeConfig nubanCondeConfig)
	{
		NubanCodeConfig nubanCodeConfig = nubanCodeConfigService.getNubanConfigObjectBasedOnParameter(nubanCondeConfig);
		return ResponseEntity.ok(nubanCodeConfig);
	}
	
	
	@RequestMapping(value = "/getNUBAN", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountNo(@RequestBody NUBANAccountDto nUBANAccountDto){
		NUBANAccountDto accountNo = nubanCodeConfigService.getAccountNo(nUBANAccountDto);
		return ResponseEntity.ok(accountNo);
	}
	
	@RequestMapping(value = "/checkConfig", method = RequestMethod.POST)
	public ResponseEntity<?> isConfigCodeExists(@RequestBody NubanCodeConfig nubanCodeConfig){
		boolean configCodeExists = nubanCodeConfigService.isConfigCodeExists(nubanCodeConfig);
		return ResponseEntity.ok(configCodeExists);
	}

}
