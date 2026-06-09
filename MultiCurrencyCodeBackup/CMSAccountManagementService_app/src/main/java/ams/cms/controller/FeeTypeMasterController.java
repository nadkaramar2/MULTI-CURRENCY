package ams.cms.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.service.FeeTypeService;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/feeTypeMaster")
public class FeeTypeMasterController {
	private AMSLogger amsLogger = AMSLogger.getInstance(FeeTypeMasterController.class);

	@Autowired
	FeeTypeService feeTypeService;

	@RequestMapping(value = "/getFeesCollected", method = RequestMethod.POST)
	public ResponseEntity<?> getFeeCollectedBalance(@RequestBody FeeTypeMaster feeTypeMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		
		try 
		{
			feeTypeMaster.setDate(Utils.getLocalDate());
			feeTypeMaster.setTime(Utils.getCurrentSqlTime());
			List<FeeTypeMaster> feeCollectedList = feeTypeService.getFeeCollectedBalance(feeTypeMaster);
			
			if (feeCollectedList != null && feeCollectedList.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("success");
				processResponse.setMessage("Data Fetched Successfully");
				processResponse.setFeesCollectedList(feeCollectedList);
			} else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("Error While Fetching Data");
			}
			return ResponseEntity.ok(processResponse);
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
}
