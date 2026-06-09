package ams.cms.txn.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.handler.impl.AccountLoadMasterHandlerImpl;
import ams.cms.logger.AMSLogger;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.utility.ThirdPartyRequestParam;
import ams.cms.utility.Utils;

@Component
public class AccountTxnEmailHandlerImpl	implements AccountTxnEmailHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountTxnEmailHandlerImpl.class);
	
	@Autowired
	private EmailService emailService;

	@Override
	public void sendCreditMailToCustomer(TransactionConfig transactionConfig)
	{
		transactionConfig.setCode("S0000");
		
		StringBuilder bodyMsg = new StringBuilder("Dear ");		
		bodyMsg.append(transactionConfig.getAccountHolderName());
		bodyMsg.append(",");
		bodyMsg.append("<br/><br/>"); 
		bodyMsg.append("Your account has been credited with transaction amount Rs.");
		bodyMsg.append(transactionConfig.getTxnAmount());
		bodyMsg.append(" bearing transaction ID ");
		bodyMsg.append(transactionConfig.getTxnId());
		bodyMsg.append(" on ");
		bodyMsg.append(Utils.simpleDateFormat4.format(Utils.getCurrentDate()));
		bodyMsg.append(" at ");
		bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentDate()));		
		bodyMsg.append("<br/><br/><br/>");
		bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
		
		transactionConfig.setMailMessage(bodyMsg.toString());	
		String subject = "Regarding to Transaction";
		sendMailToCustomer(transactionConfig, subject);
	}

	@Override
	public void sendDebitMailToCustomer(TransactionConfig transactionConfig) 
	{
		transactionConfig.setCode("S0000");
		
		StringBuilder bodyMsg = new StringBuilder("Dear ");		
		bodyMsg.append(transactionConfig.getAccountHolderName());
		bodyMsg.append(",");
		bodyMsg.append("<br/><br/>"); 
		bodyMsg.append("Your account has been debited with transaction amount Rs.");
		bodyMsg.append(transactionConfig.getTxnAmount());
		bodyMsg.append(" bearing transaction ID ");
		bodyMsg.append(transactionConfig.getTxnId());
		bodyMsg.append(" on ");
		bodyMsg.append(Utils.simpleDateFormat4.format(Utils.getCurrentDate()));
		bodyMsg.append(" at ");
		bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentDate()));		
		bodyMsg.append("<br/><br/><br/>");
		bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
		
		transactionConfig.setMailMessage(bodyMsg.toString());	
		
		String subject = "Regarding to Transaction";
		sendMailToCustomer(transactionConfig, subject);
	}
	
	@Override
	public void sendThirdPartyDebitMailToCustomer(TransactionConfig transactionConfig)
	{
		ThirdPartyRequestParam thirdPartyRequestParam = transactionConfig.getThirdPartyRequestParam();
		
		transactionConfig.setCode("S0000");
		
		StringBuilder bodyMsg = new StringBuilder("Dear ");		
		bodyMsg.append(transactionConfig.getAccountHolderName());
		bodyMsg.append(",");
		bodyMsg.append("<br/><br/>"); 
		
		amsLogger.writeInfoLog("###Inside sendThirdPartyDebitMailToCustomer applicationName::["+CommonConstants.applicationName+"]");
		if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
		{
			bodyMsg.append("Your amount NGN ");	
		}
		else
		{
			bodyMsg.append("Your amount Rs.");
		}
		
		bodyMsg.append(transactionConfig.getTxnAmount());
		bodyMsg.append(" has been successfully transferred to ");
		
		if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
		{
			if ("TPT".equalsIgnoreCase(thirdPartyRequestParam.getTxnType())) 
			{
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryBankName());
				bodyMsg.append(" ");
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryBankAccountNo());
				bodyMsg.append(" ");
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryName());
			}
			else
			{
				bodyMsg.append("VPA ID ");
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryVPAId());
			}
		}
		else 
		{
			if (thirdPartyRequestParam.getBeneficiaryBankName()!=null && thirdPartyRequestParam.getBeneficiaryBankName().trim().length() > 0)
			{
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryBankName());
				bodyMsg.append(" ");
			}
			
			bodyMsg.append(thirdPartyRequestParam.getBeneficiaryAccountNo());
			
			if (thirdPartyRequestParam.getBeneficiaryName()!=null && thirdPartyRequestParam.getBeneficiaryName().trim().length() > 0)
			{
				bodyMsg.append(" ");
				bodyMsg.append(thirdPartyRequestParam.getBeneficiaryName());
			}
		}
		
		bodyMsg.append(" on ");
		bodyMsg.append(Utils.simpleDateFormat4.format(Utils.getCurrentDate()));
		bodyMsg.append(" at ");
		bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentDate()));
		bodyMsg.append(".");
		
		bodyMsg.append("<br/><br/><br/>");
		
		/*
		if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			bodyMsg.append("Powered by Montra");
		}
		else
		{
			bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
		}
		*/
		
		transactionConfig.setMailMessage(bodyMsg.toString());	
		
		String subject = "Regarding to Transaction";
		sendMailToCustomer(transactionConfig, subject);
	}
	
	private void sendMailToCustomer(TransactionConfig transactionConfig, String subject) 
	{
		try
		{
		 	EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", transactionConfig.getEmailIdList().get(0), subject, transactionConfig.getMailMessage());
	   	  	emailTemplate.setStrParticipantid(transactionConfig.getParticipantId());
		 	emailService.sendSimpleHtmlContentMessage(emailTemplate);	   	  
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	

	@Override
	public void sendReversedMailToCustomer(TransactionConfig transactionConfig) 
	{
		StringBuilder bodyMsg = new StringBuilder("Dear ");		
		bodyMsg.append(transactionConfig.getAccountHolderName());
		bodyMsg.append(",");
		bodyMsg.append("<br/><br/>"); 
		bodyMsg.append("Your account has been credited with transaction amount ");
		
		if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			bodyMsg.append("NGN ");
		}
		else 
		{
			bodyMsg.append("Rs.");
		}
		
		bodyMsg.append(transactionConfig.getTxnAmount());
		bodyMsg.append(" bearing transaction ID ");
		bodyMsg.append(transactionConfig.getTxnId());
		bodyMsg.append(" on ");
		bodyMsg.append(Utils.simpleDateFormat4.format(Utils.getCurrentDate()));
		bodyMsg.append(" at ");
		bodyMsg.append(Utils.simpleTimeFormat1.format(Utils.getCurrentDate()));		
		
		/*
		bodyMsg.append("<br/><br/><br/>");
		if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			bodyMsg.append("Powered by Montra");
		}
		else
		{
			bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
		}
		*/
		
		transactionConfig.setMailMessage(bodyMsg.toString());
		
		String subject = "Regarding to Reverse Transaction";
		sendMailToCustomer(transactionConfig, subject);
	}
}
