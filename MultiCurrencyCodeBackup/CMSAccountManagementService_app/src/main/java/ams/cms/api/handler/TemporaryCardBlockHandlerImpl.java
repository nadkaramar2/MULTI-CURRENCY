package ams.cms.api.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.UserTransactionModel;
import ams.cms.api.service.CustomerIdService;
import ams.cms.config.TransactionConfig;
import ams.cms.model.CardAccountLinkage;
import ams.cms.model.CustomerIdCreation;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.CardAccountLinkageService;
import ams.cms.utility.Utils;

@Component
public class TemporaryCardBlockHandlerImpl implements TemporaryCardBlockHandler
{
	@Autowired
	CardAccountLinkageService cardAccountLinkageService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CustomerIdService customerIdService;
	
	@Override
	public TransactionConfig validatedCardValues(TransactionConfig transactionConfig) 
	{
		try
		{
			CardAccountLinkage cardAccountLinkage = transactionConfig.getCardAccountLinkage();
			cardAccountLinkage = cardAccountLinkageService.getLinkageCardDetailsOnCustId(cardAccountLinkage);
			
			if(cardAccountLinkage != null && "Active".equalsIgnoreCase(cardAccountLinkage.getStrCardStatus())) //add accountStatus
			{
				transactionConfig.setCardAccountLinkage(cardAccountLinkage);
				transactionConfig.setCode("S0000");
			}
			else
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Card Not Active.");
			}
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error.");
			e.printStackTrace();
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig updateCardStatusBlock(TransactionConfig transactionConfig) {
		try
		{
			CardAccountLinkage cardAccountLinkage = transactionConfig.getCardAccountLinkage();
			int updateCount = cardAccountLinkageService.updateCardStatusBlock(cardAccountLinkage);
			
			transactionConfig.setCardAccountLinkage(cardAccountLinkage);
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
		}
		return null;
	}
	
	@Override
	public TransactionConfig sendMail(TransactionConfig transactionConfig) 
	{
		try
		{
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM YYYY");
			
			StringBuilder bodyMsg = new StringBuilder("Dear ");
			StringBuilder responseMsg = new StringBuilder("");
			bodyMsg.append(transactionConfig.getUserTxnReq().getStrAccountHolderName());
			bodyMsg.append(", your card number ending with ");
			bodyMsg.append(transactionConfig.getCardAccountLinkage().getStrCardNumber()); 
			bodyMsg.append(" has been Temporary Blocked ");
			bodyMsg.append(" on "+simpleDateFormat2.format(new Date())+" at "+Utils.getFormattedCurrentTime());
			bodyMsg.append("and your reference number for this request is");
			bodyMsg.append(transactionConfig.getCardAccountLinkage());
			bodyMsg.append("Kindly retain the reference number for future use and do not loose it.");
			
			transactionConfig.setStrEmail(transactionConfig.getAccountMasterFromAccount().getStrEmailID());
			
			bodyMsg.append( "<br/><br/><br/>");
			bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
			transactionConfig.setMailMessage(bodyMsg.toString());
			
			transactionConfig.setMessage(responseMsg.toString());
			
			String subject = "Regarding to card.";
			  
		   	EmailTemplate emailTemplate =	Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", transactionConfig.getStrEmail(), subject, bodyMsg.toString());
		   	emailService.sendSimpleHtmlContentMessage(emailTemplate);	
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
		}
		return null;
	}
	
	@Override
	public TransactionConfig verifyUserPIN(TransactionConfig transactionConfig) 
	{
		try 
		{
			CardAccountLinkage cardAccountLinkage = transactionConfig.getCardAccountLinkage();
			
			String custId =	cardAccountLinkage.getStrCustId();	
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(custId);
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
			
			if (existingPIN != null)
			{
				if (!existingPIN.equals(ams.cms.utility.Utils.generateHash(cardAccountLinkage.getStrPIN()))) 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Entered PIN is Incorrect.");
				}
			}
			else 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("PIN Not Found for this account.");
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error.");
			e.printStackTrace();
		}
		return transactionConfig;
	}
	
	@Override
	public TransactionConfig updateCardStatusUnBlock(TransactionConfig transactionConfig) {
		try
		{
			CardAccountLinkage cardAccountLinkage = transactionConfig.getCardAccountLinkage();
			int updateCount = cardAccountLinkageService.updateCardStatusUnblock(cardAccountLinkage);
			
			transactionConfig.setCardAccountLinkage(cardAccountLinkage);
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
		}
		return null;
	}
}
