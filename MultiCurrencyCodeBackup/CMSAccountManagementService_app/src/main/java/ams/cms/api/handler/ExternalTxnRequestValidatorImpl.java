package ams.cms.api.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.MontraAccountMaster;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.MontraAccountMasterService;
import ams.cms.util.ProcessResponse;

@Component
public class ExternalTxnRequestValidatorImpl implements ExternalTxnRequestValidator
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ExternalTxnRequestValidatorImpl.class);
	
	@Autowired
	private MontraAccountMasterService montraAccountMasterService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private UserTransactionHandler userTransactionHandler;

	@Override
	public ProcessResponse validateMontraTxnRequest(ProcessResponse processResponse, ExternalTxnRequest externalTxnRequest) 
	{
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
			montraAccountMaster.setCid(externalTxnRequest.getCid());
			montraAccountMaster.setBid(externalTxnRequest.getBid());
			montraAccountMaster.setStrCustId(externalTxnRequest.getCustId());
			
			ProcessResponse processresp = montraAccountMasterService.validateMontraIdAndCustId(montraAccountMaster);
			if ("S0000".equalsIgnoreCase(processresp.getCode())) 
			{
				if (externalTxnRequest.getMontraTxnId() != null && externalTxnRequest.getMontraTxnId().trim().length() > 0) 
				{
					AccountTranMaster accountTranMasterInst = new AccountTranMaster(); 
					accountTranMasterInst.setStrSrcTxnId(externalTxnRequest.getMontraTxnId().trim());				
					
					AccountTranMaster accmTransMst = accountTranMasterService.getAccountTranMaster(accountTranMasterInst);						
					if (accmTransMst != null) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Duplicate Montra txn id");
					}					
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						TransactionConfig transactionConfigIns = new TransactionConfig();
						TransactionConfig secretCodeTransConfig = userTransactionHandler.validateSecretCode(transactionConfigIns);
						if (secretCodeTransConfig != null && !"S0000".equalsIgnoreCase(secretCodeTransConfig.getCode())) 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage(secretCodeTransConfig.getMessage());
						}
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Montra txn id can not be blank");
				}					
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage(processresp.getMessage());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
}
