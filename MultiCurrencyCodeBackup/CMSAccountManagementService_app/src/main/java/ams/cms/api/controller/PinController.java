package ams.cms.api.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.service.CustomerIdService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/pin")
public class PinController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(PinController.class);
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	CustomerIdService  customerIdService;
	
	@Autowired
	private EmailService emailService;
	
    @Autowired
    private ApiSecretKeyUtility apiSecretKeyUtility;
    
    @Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value ="/newPIN",method=RequestMethod.POST) 
	public ResponseEntity<?> processToSaveGenteredPinApiInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
				
			String pin = customerIdService.getCustomerPIN(customerIdCreation);			
			if (pin != null && pin.trim().length() > 0)
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("PIN Already Configured.");
			}
			else
			{
				String newPin = customerIdCreation.getStrNewPin();			
				String confirmPin = customerIdCreation.getStrConfirmNewPin();
				
				if (newPin.equals(confirmPin)) 
				{
					newPin = Utils.generateHash(newPin);
					customerIdCreation.setStrNewPin(newPin);
					
					int upateRes = customerIdService.updatePinAgainstCustId(customerIdCreation);
					if (upateRes > 0) 
					{
						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("PIN Saved Successful.");
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Issue occured during updates.");
					}
				} 
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Entered PIN is incorrect.");
				}
			}			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value ="/replacePIN",method=RequestMethod.POST) 
	public ResponseEntity<?> processToSetNewPinApiInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
				       
			String newPin = customerIdCreation.getStrNewPin();			
			String confirmPin = customerIdCreation.getStrConfirmNewPin();
			
			if (newPin.equals(confirmPin)) 
			{
				newPin = Utils.generateHash(newPin);
				customerIdCreation.setStrNewPin(newPin);
				
				int upateRes = customerIdService.updatePinAgainstCustId(customerIdCreation);
				if (upateRes > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("PIN Updated Successfully.");
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Issue occured during updates.");
				}
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Entered PIN is incorrect.");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value ="/change",method=RequestMethod.POST) 
	public ResponseEntity<?> processToSaveChangePinInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
			
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
			
			if (existingPIN != null && existingPIN.trim().length() > 0)
			{
				String currentPin = customerIdCreation.getStrCurrentPin();             
				currentPin = Utils.generateHash(currentPin);
				
				if (currentPin.equals(existingPIN)) 
				{
					String newPin = customerIdCreation.getStrNewPin();
					String confirmPin = customerIdCreation.getStrConfirmNewPin();
					
					if (newPin.equals(confirmPin)) 
					{
						newPin = Utils.generateHash(newPin);
						customerIdCreation.setStrNewPin(newPin);
						
						int upateRes = customerIdService.updatePinAgainstCustId(customerIdCreation);
						if (upateRes > 0) 
						{
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
							processResponse.setMessage("PIN updated Successful.");
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Issue occured during updates.");
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Entered PIN is incorrect!!");
					}		
				} 
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Entered PIN is incorrect!");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("PIN Not Configured. Please generate PIN First.");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value ="/forgot",method=RequestMethod.POST) 
	public ResponseEntity<?> processToSaveForgotPinInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
			
		    String existingEmailId = customerIdService.getCustomerMactchlist(customerIdCreation);
            
		    if(existingEmailId != null)
		    {
		      String emailId = customerIdCreation.getStrEmailID();		    	
		      if(emailId.equals(existingEmailId))
		      {
		    	  String otp = Utils.generateOTP(6);
		    	  System.out.println("INSIDE forgot PIN OTP IS["+otp+"]");
		    	  
		    	  customerIdCreation.setStrPinOTP(Utils.generateHash(otp));		    	  
		    	  int updateotp = customerIdService.updateOtpAgainstCustId(customerIdCreation);
		    	  
		    	  if(updateotp > 0)
		    	  {
		    		  StringBuilder bodyMsg = new StringBuilder("Dear User, ");
		    		  bodyMsg.append("<br/><br/>"); 
		    		  bodyMsg.append( "Your one time password is:"+otp+" for PIN.Please enter the OTP to proceed");
		    		  bodyMsg.append( "<br/><br/><br/>");
		    		  bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
		    		  
		    		  String subject = "Regarding to Forgot PIN";
		    		  
		    		  processResponse.setCode("S0000");
		  			  processResponse.setStatus("Success");
			    	  processResponse.setMessage("OTP Sent on your mentioned Email Id. Please check Mail.");                      
			    	  
			    	  EmailTemplate emailTemplate =	Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", emailId, subject, bodyMsg.toString());
			    	  emailService.sendSimpleHtmlContentMessage(emailTemplate);
		    	  }
		    	  else
		    	  {
		    		    processResponse.setCode("E0000");
		  			    processResponse.setStatus("Failed");
			    	    processResponse.setMessage("Error Occured While Updating OTP.");
		    	  }
		       }
		       else
		       {
		    	   processResponse.setCode("E0000");
	  			   processResponse.setStatus("Failed");
		    	   processResponse.setMessage("Entered Email id not matched.");
		       }
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Entered CustId is incorrect.");
			} 
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		} 
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value ="/verifyOTP",method=RequestMethod.POST) 
	public ResponseEntity<?> processToVeriflyOTP(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
					
			String userOTP = customerIdCreation.getStrPinOTP();
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+userOTP); 

			userOTP = Utils.generateHash(userOTP);
			
		    String existingOTP = customerIdService.getCustomerPinOTP(customerIdCreation);
         
		    if(existingOTP!=null)
		    {
		      if(existingOTP.equals(userOTP))
		       {
		    	  processResponse.setCode("S0000");
	  			  processResponse.setStatus("Success");
				  processResponse.setMessage("OTP Successfully Verified.");
		       }
		       else
		       {
		    	   processResponse.setCode("E0000");
		    	   processResponse.setStatus("Failed");
		    	   processResponse.setMessage("OTP Not Matched.");
		       }
		    }
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Entered CustId is incorrect.");
			} 
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		} 
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
}
