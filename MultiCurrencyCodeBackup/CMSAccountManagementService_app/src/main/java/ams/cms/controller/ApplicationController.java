package ams.cms.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.CommonConstants;

@RestController
@RequestMapping("/applicationName")
public class ApplicationController
{
	@RequestMapping(value = "/setApplicationName", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> setApplicationName(@RequestBody Map<Object, Object> map)
	{
		CommonConstants.applicationName = (String) map.get("appl_name");
		System.out.println(CommonConstants.applicationName);
		return ResponseEntity.ok("success");
	}
}
