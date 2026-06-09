package ams.cms.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.logger.AMSLogger;
import ams.cms.model.CloseAccountMaster;
import ams.cms.model.CloseAccountResponse;
import ams.cms.services.CloseAccountMasterService;

@RestController
@RequestMapping("/accountClosure")
public class CloseAccountMasterController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CloseAccountMasterController.class);
	
	@Autowired
	CloseAccountMasterService accountMasterService;

	//added By Sunil Y to save the request of closer
	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public ResponseEntity<?> addClosureRequest(@RequestBody CloseAccountMaster closeAccountMaster)
	{
		CloseAccountResponse closeAccountResponse = null;
		try
		{
			closeAccountResponse = accountMasterService.saveCloseAccountData(closeAccountMaster);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("In addClosureRequest Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(closeAccountResponse);
	}

	// added By Sunil Y , Maker
	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public ResponseEntity<?> makerProcessToAccountClouser(@RequestBody CloseAccountMaster closeAccountMaster)
	{
		String result = null;
		try
		{
			/*
			CloseAccountResponse closeAccountResponse = accountMasterService.makerProcessToAccountClouser(closeAccountMaster);
			return ResponseEntity.ok(closeAccountResponse);
			*/
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("test");
	}



}
