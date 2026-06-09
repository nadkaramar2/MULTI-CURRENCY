package ams.cms.api.service.impl;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.UpgradeTierReqResDao;
import ams.cms.api.model.TierUpdateDto;
import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.UpgradeTierReqResService;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

//created by ankit on 12-05-2023
@Transactional
@Service
public class UpgradeTierReqResSerivceImpl implements UpgradeTierReqResService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(UpgradeTierReqResSerivceImpl.class);
	
	@Autowired
	private UpgradeTierReqResDao upgradeTierReqResDao;
	
	@Autowired
	private CustomerIdService customerIdService;	
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public ProcessResponse addEntryInUpgradeTierReqRes(TierUpdateDto tierUpdateDto) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String strCustId = tierUpdateDto.getStrCustId();
			int verifyPin = verifyPin(tierUpdateDto);
			if(verifyPin > 0)
			{    
				UpgradeTierReqRes upgradeTierReqRes = new UpgradeTierReqRes();
				upgradeTierReqRes.setStrCustId(strCustId);
				upgradeTierReqRes.setStrTierType(tierUpdateDto.getStrUpgradeTier().toLowerCase());
				upgradeTierReqRes.setStrReqStatus("pending");
				
				/* under Cust_id getting the details of the BVN which is pending or approved
					Assuming that the BVN entry is only one in the Database table 
				*/
				
				List<UpgradeTierReqRes> upgradeTierReqResFetchedDataList = upgradeTierReqResDao.getDetails(upgradeTierReqRes);
				amsLogger.writeInfoLog("addEntryInUpgradeTierReqRes:: upgradeTierReqResFetchedDataList::"+upgradeTierReqResFetchedDataList);
				
				if(upgradeTierReqResFetchedDataList != null && upgradeTierReqResFetchedDataList.size() > 0)
				{
					UpgradeTierReqRes upgradeTierReqResData = upgradeTierReqResFetchedDataList.get(0);
					amsLogger.writeInfoLog("addEntryInUpgradeTierReqRes:: upgradeTierReqResData-"+upgradeTierReqResData);
					
					String strReqStatus = upgradeTierReqResData.getStrReqStatus();
					amsLogger.writeInfoLog("addEntryInUpgradeTierReqRes:: strReqStatus-"+strReqStatus);
					
					if(strReqStatus.equals("approved")) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Your Tier is Already updated.");
					}
					else
					{
						amsLogger.writeInfoLog("updating existing tier......");
						upgradeTierReqResData.setStrRejectedReason(" ");
						upgradeTierReqResData.setStrReqStatus("pending");
						upgradeTierReqResData.setStrResDateTime(null); 
						upgradeTierReqResData.setStrReqDateTime(Utils.getCurrentDate());
						
						updateUpgradeRequest(upgradeTierReqResData);
						
						processResponse.setStatus("Success");
						processResponse.setCode("S0000");
						processResponse.setMessage("Your upgrade to "+tierUpdateDto.getStrUpgradeTier().toUpperCase()+" has been received and your request will be processed shortly by the approving authority.");
					}		
				}
				else
				{
					processResponse = addPendingEntryInUpgradeTierReqRes(tierUpdateDto);
				}
				
				//For Sending Mail Start
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					CustomerIdCreation customerIdCreation = new CustomerIdCreation();
					customerIdCreation.setStrCustId(strCustId);				
					customerIdCreation = customerIdService.getCustomerInformationByCustId(customerIdCreation);
					
					amsLogger.writeInfoLog("addEntryInUpgradeTierReqRes:: customerIdCreation-"+customerIdCreation);
					String subject = "Upgrade Request for "+ tierUpdateDto.getStrUpgradeTier().toUpperCase();				
					sendTierUpgradationMailToGroup(customerIdCreation, subject);
				}
				//For Sending Mail End
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("Invalid PIN. Please Enter valid PIN.");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("Inside addEntryInUpgradeTierReqRes:: processResponse-"+processResponse);
		return processResponse;
	}


	private ProcessResponse addPendingEntryInUpgradeTierReqRes(TierUpdateDto tierUpdateDto)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			UpgradeTierReqRes upgradeTierReqResData = mapDtoToUpgradeTierReqRes(tierUpdateDto);
			
			int count =	upgradeTierReqResDao.addEntryInUpgradeTierReqRes(upgradeTierReqResData);
			if (count > 0) 
			{
				processResponse.setStatus("Success");
				processResponse.setCode("S0000");
				processResponse.setMessage("Your upgrade to "+tierUpdateDto.getStrUpgradeTier().toUpperCase()+" has been received and your request will be processed shortly by the approving authority.");
			}
			else
			{
				processResponse.setStatus("Failed");
				processResponse.setCode("E0000");
				processResponse.setMessage("Error during adding upgrade request.");
			}
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}


	private UpgradeTierReqRes mapDtoToUpgradeTierReqRes(TierUpdateDto tierUpdateDto) 
	{
		UpgradeTierReqRes upgradeTierReqRes = new UpgradeTierReqRes();
		
		upgradeTierReqRes.setStrCustId(tierUpdateDto.getStrCustId());
		upgradeTierReqRes.setStrTierType(tierUpdateDto.getStrUpgradeTier());
		upgradeTierReqRes.setStrReqStatus("pending");
		upgradeTierReqRes.setStrReqDateTime(Utils.getCurrentDate());
		
		return upgradeTierReqRes;
	}

	private int verifyPin(TierUpdateDto tierUpdateDto) 
	{
		try
		{
			String strPin = tierUpdateDto.getStrPin();
			String enteredPIN = ams.cms.utility.Utils.generateHash(strPin);
			
			CustomerIdCreation customerOBJ = new CustomerIdCreation();
			customerOBJ.setStrCustId(tierUpdateDto.getStrCustId()); 
			
			String customerPIN = customerIdService.getCustomerPIN(customerOBJ);			
			if(enteredPIN.equalsIgnoreCase(customerPIN))
			{
				return 1;
			}	
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	private void sendTierUpgradationMailToGroup(CustomerIdCreation customerIdCreation, String subject) 
	{
		StringBuilder bodyMsg = new StringBuilder("Dear User, ");
		bodyMsg.append("<br/><br/>");		
		bodyMsg.append("Request for upgrade has been received from ");
		bodyMsg.append(customerIdCreation.getStrCustomerName());
		bodyMsg.append(" having Cust Id ");
		bodyMsg.append(customerIdCreation.getStrCustId());
		bodyMsg.append("<br/><br/>");	
		bodyMsg.append("Kindly Login to System and Approve.");
		
		//bodyMsg.append("<br/><br/>");
		//bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
		
		sendMailToMultiplePerson(bodyMsg.toString(), subject);
	}
	
	//copy pasted method from the signup controller
	private void sendMailToMultiplePerson(String bodyMsg, String subject) 
	{
		try
		{
			ArrayList<String> toMailId = Utils.getGroupMailId();
			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMailToMultiple("contactus@AMStechnologies.com", toMailId, subject, bodyMsg);
			emailService.sendSimpleHtmlContentMessage(emailTemplate);
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	//copy pasted method from the signup controller


	@Override
	public int updateUpgradeRequest(UpgradeTierReqRes upgradeTierReqRes) 
	{
		return upgradeTierReqResDao.updateUpgradeRequest(upgradeTierReqRes);
	}
}
