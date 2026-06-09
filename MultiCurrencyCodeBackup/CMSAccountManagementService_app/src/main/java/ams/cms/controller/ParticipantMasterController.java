package ams.cms.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantMaster;
import ams.cms.services.ParticipantMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.util.ProcessWebResponse;

@RestController
@RequestMapping("/participant_master")
public class ParticipantMasterController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ParticipantMasterController.class);
	
	@Autowired
	private ParticipantMasterService participantMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@RequestBody ParticipantMaster participantMaster)
	{
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		try
		{
			ParticipantMaster participantMasterObj = participantMasterService.addParticipant(participantMaster);
			
			  if(participantMasterObj != null)
			  { 
				  processWebResponse.setCode("S0000");
				  processWebResponse.setStatus("Sucess");
				  processWebResponse.setMessage("Data Successfully Save.");
			  }
			  else 
			  {
				  processWebResponse.setCode("E0000"); 
				  processWebResponse.setStatus("Failed");
				  processWebResponse.setMessage("Data Not Saved "); 
			  }
		}
		catch (Exception e)
		{
			System.out.println("Exception in ::"+e);
			processWebResponse.setCode("E0000");
			processWebResponse.setStatus("Failed");
			processWebResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
			return ResponseEntity.ok(processWebResponse);
	}
	
	@RequestMapping(value = "/generateApiKey", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantWithApikeyGeneration(@RequestBody ParticipantMaster participantMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ParticipantMaster participantMasterObj = participantMasterService.addParticipant(participantMaster);
			if (participantMasterObj != null && participantMasterObj.getStrApikey() != null) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Successfully api key generated.");
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Error Occured during api key generation.");
			}
			
			//processResponse.setParticipantApiKey(participantMasterObj.getStrApikey());
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
	
	@RequestMapping(value = "/getAllParticipantList", method = RequestMethod.POST)
	public ResponseEntity<?> getAllParticipantList(@RequestBody ParticipantMaster participantMaster)
	{
		ProcessWebResponse processWebResponse = new ProcessWebResponse();
		try
		{
			 List<ParticipantMaster> participantMasterList =   participantMasterService.getAllParticipantList(participantMaster);
			
			  if(participantMasterList != null)
			  { 
				  //processWebResponse.setParticipantMasterObj(participantMasterList);
				  processWebResponse.setCode("S0000");
				  processWebResponse.setStatus("Sucess");
				  processWebResponse.setMessage("Data Successfully Retrieved.");
			  }
			  else 
			  {
				  processWebResponse.setCode("E0000"); 
				  processWebResponse.setStatus("Failed");
				  processWebResponse.setMessage("Data Not Retrieved "); 
			  }
		}
		catch (Exception e)
		{
			System.out.println("Exception in ::"+e);
			processWebResponse.setCode("E0000");
			processWebResponse.setStatus("Failed");
			processWebResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
			return ResponseEntity.ok(processWebResponse);
	}
}
