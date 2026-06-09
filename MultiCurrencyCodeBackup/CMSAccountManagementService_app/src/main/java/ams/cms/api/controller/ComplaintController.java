package ams.cms.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountRequest;
import ams.cms.api.model.Complaint;
import ams.cms.api.model.ComplaintDisplayRequest;
import ams.cms.api.model.ComplaintRequest;
import ams.cms.api.model.ComplaintResolveRequest;
import ams.cms.api.model.ComplaintType;
import ams.cms.api.model.PendingComplaintResponse;
import ams.cms.api.service.ComplaintService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

//added by ankit
@RestController
@RequestMapping("/complaint")
public class ComplaintController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ComplaintController.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;

	@Autowired
	private ComplaintService complaintService;

	@Autowired 
	JwtUtil jwtUtil;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;

	@RequestMapping(value = "/raise", method = RequestMethod.POST)
	public ResponseEntity<?> raiseComplaint(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}

			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ComplaintRequest complaint = new ObjectMapper().readValue(decrypt, ComplaintRequest.class);

			int raiseComplaintCheck = complaintService.raiseComplaint(complaint);
			if (raiseComplaintCheck > 0)
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Complaint Has Been Raised successful!");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Complaint Could Not Be Raised Please Try Again!");
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/id", method = RequestMethod.POST)
	public ResponseEntity<?> getComplaintById(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ComplaintDisplayRequest complaint = new ObjectMapper().readValue(decrypt, ComplaintDisplayRequest.class);
			
			Complaint complaintDetails = complaintService.findComplaintByComplaintId(complaint);
			List<Complaint> complaintDetailsAsList = Arrays.asList(complaintDetails);
			String strComplaintStatus = complaintDetails.getStrComplaintStatus();
			
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			processResponse.setMessage("Complaint Details");
			
			if (strComplaintStatus.equals("Pending")) 
			{
				PendingComplaintResponse pendingComplaintResponse = mapComplaintToPendingComplaintResponse(complaintDetails);
				List<PendingComplaintResponse> pendingComplaintResponseAsList = Arrays.asList(pendingComplaintResponse);
				processResponse.setPendingComplaintResponse(pendingComplaintResponseAsList);
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
			
			processResponse.setComplaint(complaintDetailsAsList);
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<?> getComplaintById(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);
			
			List<Complaint> findAllComplaints = complaintService.findAllComplaints();
			if (findAllComplaints!=null && !findAllComplaints.isEmpty())
			{
				processResponse.setComplaint(findAllComplaints);
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("All Complaints");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
			
			processResponse.setCode("E0000");
			processResponse.setStatus("Error");
			processResponse.setMessage("Complaint Not Found");
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/resolve", method = RequestMethod.POST)
	public ResponseEntity<?> resolveComplaint(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			ComplaintResolveRequest complaintResolve = new ObjectMapper().readValue(decrypt, ComplaintResolveRequest.class);
			
			int resolveComplaints = complaintService.resolveComplaint(complaintResolve);
			if (resolveComplaints > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Complaint Resolved");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Error");
				processResponse.setMessage("Complaint Cannot Be Resolved");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Error");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// view Complaint Types
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public ResponseEntity<?> getAllComplaintTypes(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);
			
			List<ComplaintType> allComplaintTypes = complaintService.findAllComplaintTypes();
			if (allComplaintTypes!=null && !allComplaintTypes.isEmpty()) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("All Complaints Types");
				
				//processResponse.setComplaintTypes(allComplaintTypes);
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
			
			processResponse.setCode("E0000");
			processResponse.setStatus("Error");
			processResponse.setMessage("Complaint Types Not Found");
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	public PendingComplaintResponse mapComplaintToPendingComplaintResponse(Complaint complaint) 
	{
		String strComplaintDescription = complaint.getStrComplaintDescription();
		String strComplaintType = complaint.getStrComplaintType();
		String strComplaintId = complaint.getStrComplaintId();
		String strComplaintStatus = complaint.getStrComplaintStatus();
		PendingComplaintResponse pendingComplaintResponse = new PendingComplaintResponse();
		pendingComplaintResponse.setStrComplaintDescription(strComplaintDescription);
		pendingComplaintResponse.setStrComplaintId(strComplaintId);
		pendingComplaintResponse.setStrComplaintStatus(strComplaintStatus);
		pendingComplaintResponse.setStrComplaintType(strComplaintType);
		return pendingComplaintResponse;
	}
	
	@RequestMapping(value = "/getComplaintOfAccount", method = RequestMethod.POST)
	public ResponseEntity<?> allComplaintsOfAccountNumber(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountRequest accountRequest = new ObjectMapper().readValue(decrypt, AccountRequest.class);
			List<String> allComplaintsOfAccount = complaintService.getAllComplaintsOfAccount(accountRequest);
			if (allComplaintsOfAccount!=null && !allComplaintsOfAccount.isEmpty()) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setComplaitIdList(allComplaintsOfAccount);
				processResponse.setMessage("Complaint Id's Done By User");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Complaints By User"); 
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/allTypes", method = RequestMethod.POST)
	public ResponseEntity<?> getAllComplaintTypesList(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
				String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
				AccountRequest accountRequest = new ObjectMapper().readValue(decrypt, AccountRequest.class);
				List<String> allComplaintTypes = complaintService.findAllComplaintBasedOnType(accountRequest);
				if (allComplaintTypes!=null && !allComplaintTypes.isEmpty())
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Complaint Types");
					processResponse.setComplaintTypes(allComplaintTypes);
					return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
				}
				processResponse.setCode("E0000");
				processResponse.setStatus("Error");
				processResponse.setMessage("Complaint Types Not Found");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
}
