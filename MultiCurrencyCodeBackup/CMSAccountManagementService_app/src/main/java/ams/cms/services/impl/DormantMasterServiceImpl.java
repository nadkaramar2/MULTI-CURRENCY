package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import ams.cms.api.handler.TransactionHandlerAPI;
import ams.cms.config.TransactionConfig;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.CustomerIDCreationDao;
import ams.cms.dao.DormantAccountReqResStatusDao;
import ams.cms.dao.DormantMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.DormantAccountInformation;
import ams.cms.model.DormantAccountMaster;
import ams.cms.model.DormantToActiveMaster;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.DormantMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Transactional
@Service
@Component("DormantMasterService")
public class DormantMasterServiceImpl implements DormantMasterService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(DormantMasterServiceImpl.class);
	
	@Autowired
	private AccountMasterDao accountMasterDao;
	
	@Autowired
	private TransactionHandlerAPI transactionHandlerAPI;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private DormantMasterDao dormantMasterDao;
	
	@Autowired
	private DormantAccountReqResStatusDao dormantToActiveDao;
	
	@Autowired
	private CustomerIDCreationDao customerIDCreationDao;
	

	@Override
	public ProcessResponse DormancyValidationProcess(AccountCreation accountMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		if(accountMaster != null && accountMaster.getStrAccountNumber() != null) 
		{
			AccountCreation accountCreation = accountMasterDao.getAccountInformation(accountMaster);
			if(accountCreation != null && accountCreation.getStrAccountNumber().equalsIgnoreCase(accountMaster.getStrAccountNumber())) 
			{
				if("Dormant".equalsIgnoreCase(accountCreation.getStrStatus())) 
				{
					DormantAccountMaster dormantAccountMaster = new DormantAccountMaster();
					dormantAccountMaster.setAccountNumber(accountMaster.getStrAccountNumber());
					dormantAccountMaster = dormantMasterDao.getAccountInformantion(dormantAccountMaster);
					
					if(dormantAccountMaster != null) 
					{
						DormantAccountInformation dormantAccountInformation = new DormantAccountInformation();
						
						dormantAccountInformation.setAccountHolderName(dormantAccountMaster.getStrAccountHolderName());
						dormantAccountInformation.setAccountType(dormantAccountMaster.getAccountType());
						dormantAccountInformation.setClosingBalance(dormantAccountMaster.getClosingBalance());
						dormantAccountInformation.setCreationDate(dormantAccountMaster.getCreationDate());
						dormantAccountInformation.setDescription(dormantAccountMaster.getDescription());
						dormantAccountInformation.setDormantMarkedDate(dormantAccountMaster.getDormantMarkedDate());
						dormantAccountInformation.setLastTxnDate(dormantAccountMaster.getLastTxnDate());
						dormantAccountInformation.setStatus(dormantAccountMaster.getStatus());
						
						processResponse.setCode("S0000");
						processResponse.setMessage("Data Fetch Scussefully");
						processResponse.setDormantAccountInformation(dormantAccountInformation);
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setMessage("Data Not Fetch");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setMessage("Account Not Dormant");	
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("Account Not Found By This Account Number");	
			}
		}
		else
		{
			processResponse.setCode("E0000");
			processResponse.setMessage("Please Enter Account Number");
		}
		return processResponse;
	}

	@Override
	public ProcessResponse makerProcessForDormancy(DormantAccountMaster dormantAccountMaster) 
	{
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
		//check if Aredy Exist
		DormantToActiveMaster dormantToActiveMasterObj = new DormantToActiveMaster();
		dormantToActiveMasterObj.setAccountNumber(dormantAccountMaster.getAccountNumber());
		dormantToActiveMasterObj = dormantToActiveDao.findDormantToActiveRequest(dormantToActiveMasterObj);
		if(dormantToActiveMasterObj == null) {
		//check Account Exist or not
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountNumber(dormantAccountMaster.getAccountNumber());
		accountCreation = accountMasterDao.getAccountInformation(accountCreation);
		if(accountCreation != null && accountCreation.getStrAccountNumber() != null) {
		
		// We will get AcconutNo , Account Type , dormantMarkedDate , MakerId , reason
		if(dormantAccountMaster != null && dormantAccountMaster.getAccountNumber() != null) {
			DormantToActiveMaster dormantToActiveMaster = new DormantToActiveMaster();
			dormantToActiveMaster.setAccountNumber(dormantAccountMaster.getAccountNumber());
			dormantToActiveMaster.setAccountType(dormantAccountMaster.getAccountType());
			dormantToActiveMaster.setDormantMarkedDate(Utils.getCurrentDate());
			dormantToActiveMaster.setReasonForActive(dormantAccountMaster.getReason());
			dormantToActiveMaster.setRequestDate(Utils.getCurrentDate());
			dormantToActiveMaster.setRequestTime(Utils.getCurrentSqlTime());
			dormantToActiveMaster.setRequestRaisedBy(dormantAccountMaster.getMakerId());
			dormantToActiveMaster.setStatus("Pending");
			dormantToActiveDao.save(dormantToActiveMaster);
			if(dormantToActiveMaster.getId() != null) {
				processResponse.setCode("S0000");
				processResponse.setMessage("Data Saved Scussefully");
			}else {
				processResponse.setCode("E0000");
				processResponse.setMessage("Issue Wile Saving Data");
			}
		}else {
			processResponse.setCode("E0000");
			processResponse.setMessage("Request Data Empty");
		}
		}else {
			processResponse.setCode("E0000");
			processResponse.setMessage("Account Not Found");
		}
		}else {
			processResponse.setCode("E0000");
			processResponse.setMessage("Account Already Exist");
		}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return processResponse;
	}

	@Override
	public ProcessResponse checkerProcessForDormancy(DormantAccountMaster dormantAccountMaster)
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		
		//check if status is not null and reason 
		if(dormantAccountMaster !=null && dormantAccountMaster.getStatus() != null && dormantAccountMaster.getReason() != null) 
		{
			DormantToActiveMaster dormantToActiveMaster = new DormantToActiveMaster();
			dormantToActiveMaster.setAccountNumber(dormantAccountMaster.getAccountNumber());
			
			DormantToActiveMaster drmantToActiveMaster = dormantToActiveDao.findDormantToActiveRequest(dormantToActiveMaster);			
			if(drmantToActiveMaster != null && "Authorized".equalsIgnoreCase(drmantToActiveMaster.getStatus())) 
			{
				if(dormantToActiveMaster != null && dormantToActiveMaster.getAccountNumber() != null) 
				{
					dormantToActiveMaster.setRequestAuthorisedBy(dormantAccountMaster.getCheckerId());
					dormantToActiveMaster.setRequestAuthorisedDate(Utils.getCurrentDate());
					dormantToActiveMaster.setRequestAuthorisedTime(Utils.getCurrentSqlTime());
					dormantToActiveMaster.setRequestAuthorisedReason(dormantAccountMaster.getReason());
					dormantToActiveMaster.setAccountNumber(dormantAccountMaster.getAccountNumber());
					dormantToActiveMaster.setStatus(dormantAccountMaster.getStatus());					
					dormantToActiveDao.updateDormantToActiveByAccountNumber(dormantToActiveMaster);		
					
					//update Dormant Account Table
					DormantAccountMaster dormantAccountMasterObj = new DormantAccountMaster();
					dormantAccountMasterObj.setAccountNumber(dormantAccountMaster.getAccountNumber());
					dormantAccountMasterObj = dormantMasterDao.getDormantAccountInformantion(dormantAccountMasterObj);
					if(dormantAccountMasterObj != null && dormantAccountMasterObj.getAccountNumber() != null && dormantAccountMasterObj.getId() != null) 
					{
						dormantAccountMasterObj.setAccountDormantReleasedDate(Utils.getCurrentDate());
						dormantAccountMasterObj.setDormantReleasedReason(dormantAccountMaster.getReason());
						dormantAccountMasterObj.setReleasedBy(dormantAccountMaster.getCheckerId());
						dormantAccountMasterObj.setAccountNumber(dormantAccountMaster.getAccountNumber());
						dormantMasterDao.updateDormantAccountMasterByAccountNo(dormantAccountMasterObj);
						
						//update AccountMaster
						AccountCreation accountCreation = new AccountCreation();
						accountCreation.setStrAccountNumber(dormantAccountMaster.getAccountNumber());
						
						AccountCreation accountCreat = accountMasterDao.getAccountInformation(accountCreation);
						if(accountCreat != null && accountCreat.getStrID() != null) 
						{
							accountCreation.setDormantMarkedDate(null);
							accountCreation.setDormantReleasedDate(Utils.getCurrentDate());
							accountCreation.setStrStatus("Active");
							accountMasterDao.updateAccountMasterForDormant(accountCreation);
							
							//send Mail To Customer
							if (accountCreat.getStrEmailID()!=null && accountCreat.getStrEmailID().trim().length() > 0)
							{
								sendMailToCustomer(accountCreat.getStrEmailID(),accountCreat.getStrAccountHolderName(), accountCreat.getStrAccountNumber() , accountCreat.getStrAccountType());
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Account Found");
						}
					}
					else 
					{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("No Dormant Account Found");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("No Account Found");
				}
			}
			else if("Reject".equalsIgnoreCase(dormantAccountMaster.getStatus())) 
			{
				if(dormantToActiveMaster != null && dormantToActiveMaster.getAccountNumber() != null) 
				{
					dormantToActiveMaster.setRequestRejectedBy(dormantAccountMaster.getCheckerId());
					dormantToActiveMaster.setRequestRejectedTime(Utils.getCurrentSqlTime());
					dormantToActiveMaster.setRequestRejectedDate(Utils.getCurrentDate());
					dormantToActiveMaster.setRequestRejectedReason(dormantAccountMaster.getReason());
					dormantToActiveMaster.setStatus(dormantAccountMaster.getStatus());
					
					dormantToActiveDao.updateDormantToActiveByAccountNumberForReject(dormantToActiveMaster);	
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Unknown Status");
			}
		}
		else
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Unknown Status");
		}
		return processResponse;
	}
	
	public boolean sendMailToCustomer(String custEmailId, String accountHolderName , String accountNo ,String accountType) 
	{
		try 
		{
			EmailTemplate emailTemplate = new EmailTemplate();
			
			String subject = "Dormant To Active Account";
			StringBuilder bodyMsg = new StringBuilder("Dear User,<br/><br/>");
			bodyMsg.append("Dear " +accountHolderName +" Your Account Number "+accountNo+ " of Account type "+accountType+" which was marked Dormant has now been marked as active. Kindly perform transactions as appropriate. " );
			bodyMsg.append("</b><br/><br/><br/>");			
			bodyMsg.append("Powered by AMS Technologies Pvt Ltd.");
			emailTemplate.setStrFrom("contactus@AMStechnologies.com");
			emailTemplate.setStrTo(custEmailId);
			emailTemplate.setStrSubject(subject);
			emailTemplate.setStrText(bodyMsg.toString());
		
			String result =	emailService.sendSimpleHtmlContentMessage(emailTemplate);
			if (result!=null) 
			{
				return true;
			}
		}
		catch (Exception e) 
		{
			
		}
		return false;
	}

	@Override
	public ProcessResponse makerProcessForMobileDormancy(AccountCreation accountCreation) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try {
		
		//chek the cust id is present or not 
		if(accountCreation != null && accountCreation.getStrCustId() != null) {
			CustomerIdCreation  customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(accountCreation.getStrCustId());
			CustomerIdCreation customerIdCreationObj = customerIDCreationDao.getCustomerIdInfo(customerIdCreation);
					//getCustomerIdInfo
			
			if(customerIdCreationObj != null && customerIdCreationObj.getStrCustId() != null) {
			
				accountCreation.setStrCustPin(accountCreation.getStrCustPin());
				TransactionConfig transactionHandlerAPIObj = transactionHandlerAPI.verifyCustomerPin(accountCreation);
				
				if(transactionHandlerAPIObj.getCode().equalsIgnoreCase("S0000")) {
				
			AccountCreation accountMaster = new AccountCreation();
			accountMaster.setStrAccountNumber(accountCreation.getStrAccountNumber());
			accountMaster.setStrCustId(accountCreation.getStrCustId());
			AccountCreation accountMasterObj = accountMasterDao.getAccountInformation(accountMaster);
			
			if(accountMasterObj != null && accountMasterObj.getStrCustId()!= null && accountMasterObj.getStrAccountNumber()!= null) {
				
			if("Dormant".equalsIgnoreCase(accountMasterObj.getStrStatus())) {
				
				DormantToActiveMaster dormantToActiveMasterObj = new DormantToActiveMaster();
				dormantToActiveMasterObj.setAccountNumber(accountMasterObj.getStrAccountNumber());
				dormantToActiveMasterObj = dormantToActiveDao.findDormantToActiveRequest(dormantToActiveMasterObj);
				if(dormantToActiveMasterObj != null && dormantToActiveMasterObj.getAccountNumber() !=null) {
					processResponse.setCode("E0000");
					processResponse.setMessage("Accout Request Already Added.");
					return processResponse;
				}
				
				   //Entry in DormantToActive
					DormantToActiveMaster dormantToActiveMaster = new DormantToActiveMaster();
					dormantToActiveMaster.setAccountNumber(accountMasterObj.getStrAccountNumber());
					dormantToActiveMaster.setAccountType(accountMasterObj.getStrAccountType());
					dormantToActiveMaster.setDormantMarkedDate(accountMasterObj.getDormantMarkedDate());
					dormantToActiveMaster.setRequestDate(Utils.getCurrentDate());
					dormantToActiveMaster.setRequestTime(Utils.getCurrentSqlTime());
					dormantToActiveMaster.setRequestRaisedBy(accountMasterObj.getStrCustId());
					dormantToActiveMaster.setReasonForActive(accountCreation.getReason());
					dormantToActiveMaster.setStatus("Pending");
					dormantToActiveDao.save(dormantToActiveMaster);
					
					//send email to Backoffice
					accountMasterObj.setReason(accountCreation.getReason());
					sendMailToGroup(accountMasterObj,dormantToActiveMaster);
					
					processResponse.setCode("S0000");
					processResponse.setMessage("Request Added Scussefully");
				}else {
					processResponse.setCode("S0000");
					processResponse.setMessage("Account Status is Active");
				}
			}else {
				processResponse.setCode("E0000");
				processResponse.setMessage("Account Not Found");
			}
		}else {
			processResponse.setCode("E0000");
			processResponse.setMessage("Wrong Pin");
		}
			}else {
				processResponse.setCode("E0000");
				processResponse.setMessage("No Customer Account Found");
			}
		}else {
			processResponse.setCode("E0000");
			processResponse.setMessage("Customer Not Exist");
		}
		
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return processResponse;
	}

	@Override
	public ProcessResponse checkerProcessValidation(AccountCreation accountCreation)
	{
		ProcessResponse processResponse = new ProcessResponse();
		if(accountCreation != null && accountCreation.getStrAccountNumber() != null) 
		{
			AccountCreation accountCreationObj = accountMasterDao.getAccountInformation(accountCreation);
			if(accountCreationObj != null && accountCreationObj.getStrAccountNumber() != null)
			{
				DormantAccountInformation dormantAccountInformation = new DormantAccountInformation();
				dormantAccountInformation.setAccountHolderName(accountCreationObj.getStrFirstName() +" "+accountCreationObj.getStrLastName());
				dormantAccountInformation.setAccountType(accountCreationObj.getStrAccountType());
				dormantAccountInformation.setClosingBalance(accountCreationObj.getStrClosingBalance());
				dormantAccountInformation.setCreationDate(String.valueOf(accountCreationObj.getStrDateOfCreation()));
				dormantAccountInformation.setDescription(accountCreationObj.getStrAccountType());
				dormantAccountInformation.setDormantMarkedDate(String.valueOf(accountCreationObj.getDormantMarkedDate()));
				dormantAccountInformation.setLastTxnDate(String.valueOf(accountCreationObj.getLastTxnDate()));
				dormantAccountInformation.setStatus(accountCreationObj.getStrStatus());
				dormantAccountInformation.setAccountNumber(accountCreationObj.getStrAccountNumber());
				
				processResponse.setCode("S0000");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setDormantAccountInformation(dormantAccountInformation);
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("Account Not Exist");
			}
		}
		else 
		{
			processResponse.setCode("E0000");
			processResponse.setMessage("Account Number Not Found");
		}
		return processResponse;
	}
	
	
	private void sendMailToGroup(AccountCreation accountCreation, DormantToActiveMaster dormantToActiveMaster) 
	{
		try 
		{
			StringBuilder bodyMsg = new StringBuilder("Dear User,");
			bodyMsg.append("<br/><br/>");
			bodyMsg.append("Account Request for Dormant To Active  has been initiated by Customer ");
			bodyMsg.append(accountCreation.getStrCustId());
			bodyMsg.append(" on " );
			bodyMsg.append(Utils.simpleDateFormat4.format(new Date()));
			bodyMsg.append(" ");
			bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentSqlTime()));
			bodyMsg.append(" for ");
			bodyMsg.append(accountCreation.getStrAccountType());
			bodyMsg.append(" having account Number ");
			bodyMsg.append(accountCreation.getStrAccountNumber());
			bodyMsg.append(".");
			bodyMsg.append("<br/>");
			bodyMsg.append("<br/>");
			bodyMsg.append("Please note the reason for ");
			bodyMsg.append(accountCreation.getReason());
			bodyMsg.append(".");
			bodyMsg.append("<br/>");
			bodyMsg.append("Kindly log into back-office and Approve/Reject the Authorise Dormant To Active .");
			
			String emailSubject = "Authorise Dormant To Active";
			System.out.println(bodyMsg.toString());
			sendMailToMultiplePerson(bodyMsg.toString(), emailSubject);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void sendMailToMultiplePerson(String bodyMsg, String subject) 
	{
		try 
		{
			ArrayList<String> toMailId = Utils.getGroupMailId();
			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMailToMultiple("contactus@AMStechnologies.com", toMailId, subject, bodyMsg);
			emailService.sendSimpleHtmlContentMessage(emailTemplate);
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public ProcessResponse dormantStatusByAccountNo(DormantAccountMaster dormantAccountMaster) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		if(dormantAccountMaster != null && dormantAccountMaster.getAccountNumber() != null) 
		{
			DormantAccountMaster dormantAccountMasters = dormantMasterDao.getDormantStatusByAccountNumber(dormantAccountMaster);
			if(dormantAccountMasters != null && dormantAccountMasters.getAccountNumber() != null) 
			{
				processResponse.setDormantAccountMaster(dormantAccountMaster);
				processResponse.setCode("E0000");
				processResponse.setCode("Data Fetch Scussefully");
			}
		}
		else 
		{
			processResponse.setCode("E0000");
			processResponse.setCode("No Data Found");
		}
		return processResponse;
	}
}
