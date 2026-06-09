package ams.cms.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.VatTypeMaster;
import ams.cms.api.service.VatTypeService;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/vatTypeMaster")
public class VatTypeMasterController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(VatTypeMasterController.class);
	
	@Autowired
	VatTypeService vatTypeService;
	
	
	@RequestMapping(value = "/getVatCollected", method = RequestMethod.POST)
	public ResponseEntity<?> getVatCollectedBalance(@RequestBody VatTypeMaster vatTypeMaster)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			vatTypeMaster.setDate(Utils.getLocalDate());
			vatTypeMaster.setTime(Utils.getCurrentSqlTime());
			List<VatTypeMaster> vatCollectedList = vatTypeService.getVatCollectedBalance(vatTypeMaster);
			if(vatCollectedList !=null && vatCollectedList.size() > 0)
			{
			processResponse.setCode("S0000");
			processResponse.setStatus("success");
			processResponse.setMessage("Data Fetched Successfully");
			processResponse.setVatCollectedList(vatCollectedList);
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("Error While Fetching Data");
			}
			return ResponseEntity.ok(processResponse);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
}
