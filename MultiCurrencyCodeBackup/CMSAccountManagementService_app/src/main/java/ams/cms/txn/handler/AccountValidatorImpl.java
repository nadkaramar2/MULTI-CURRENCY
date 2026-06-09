package ams.cms.txn.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.service.CustomerIdService;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.services.AccountMasterService;
import ams.cms.util.ProcessResponse;

@Component
public class AccountValidatorImpl implements AccountValidator 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountValidatorImpl.class);
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private CustomerIdService customerIdService; 

	@Override
	public TransactionConfig validateAccountStatus(TransactionConfig transactionConfig)
	{
		try
		{
				
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public ProcessResponse validateCustomerPIN(ProcessResponse processResponse, CustomerIdCreation customerIdCreation) 
	{
		try
		{
			String customerEnterPIN = customerIdCreation.getCustomerEnterPIN();
			
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
			if (existingPIN != null) 
			{
				if(!existingPIN.equals(ams.cms.utility.Utils.generateHash(customerEnterPIN))) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Entered PIN is Incorrect.");
				}										
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("PIN Not Found Against this cust Id.");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
