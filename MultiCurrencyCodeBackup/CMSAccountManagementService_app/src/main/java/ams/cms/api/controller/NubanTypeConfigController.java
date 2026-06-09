package ams.cms.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.NubanTypeConfig;
import ams.cms.api.service.NubanTypeConfigService;



@RestController
@RequestMapping(value = "/nubanType")
public class NubanTypeConfigController {

	@Autowired
	private NubanTypeConfigService nubanTypeConfigService;

	@RequestMapping(value = "/getType", method = RequestMethod.GET)
	public ResponseEntity<?> getNubanTypes(){
		List<NubanTypeConfig> nubanTypes = nubanTypeConfigService.getNubanTypes();
		return ResponseEntity.ok(nubanTypes);
	}
	
	@RequestMapping(value = "/getDescription", method = RequestMethod.POST)
	public ResponseEntity<?> getNubanTypeDescription(@RequestBody NubanTypeConfig nubanTypeConfig){
		NubanTypeConfig nubanTypes = nubanTypeConfigService.getNubanTypeDescription(nubanTypeConfig);
		return ResponseEntity.ok(nubanTypes);
	}
	
}
