package ams.cms.txn.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ams.cms.config.CommonConstants;
import ams.cms.handler.LoadMasterHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.FundTransferIn;
import ams.cms.model.TransactionTypeModel;
import ams.cms.model.TxnReqRes;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.TransactionTypeService;
import ams.cms.util.FundTransferInConstant;
import ams.cms.util.FundTransferProcessResponse;

@Service
public class FundTransferInService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(FundTransferInService.class);
	
	@Autowired
	private LoadMasterHandler loadMasterHandler;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private	TransactionTypeService transactionTypeService;
	
	@Async("multiThreadBean")
	public CompletableFuture<FundTransferIn> processToLoadBalnceIN(FundTransferIn fundTransferIn) throws InterruptedException, ExecutionException
	{
		CommonConstants.applicationName = "NIGERIA";
		FundTransferProcessResponse fundTransferProcessResponse = new FundTransferProcessResponse();
		try
		{ 
			//Added by sunny soni for request validation Start
			fundTransferProcessResponse.setCode("S0000");
			
			String tranType = fundTransferIn.getTranType();
			tranType = (tranType!=null && tranType.trim().length() > 0) ? tranType.trim() : null; 
			
			amsLogger.writeInfoLog("Before fundTransferIn Tran Type=["+tranType+"]");
			if (tranType != null && "FNW".equalsIgnoreCase(tranType)) 
			{
				//Added by Sagar Khawse for getting participantId of FNW Start
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setStrTxnTypeKeyWord(tranType.trim());
				
				transactionTypeModel = transactionTypeService.getTransactionTypeMasterBasedOnTxnType(transactionTypeModel);
				String participantId = "";
				if (transactionTypeModel!=null && transactionTypeModel.getStrParticipantId()!=null && transactionTypeModel.getStrParticipantId().trim().length() > 0)
				{
					participantId = transactionTypeModel.getStrParticipantId().trim();
				}
				fundTransferIn.setParticipantId(participantId);
				//Added by Sagar Khawse for getting participantId of FNW End					
				
				tranType = FundTransferInConstant.FNW;
				fundTransferProcessResponse = validateFundRequest(fundTransferProcessResponse, fundTransferIn);
			}
			//Added by sunny soni for request validation End
			
			fundTransferIn.setTranType(tranType);//Added hard code entry for Fund Transfer In Wallet
			amsLogger.writeInfoLog("After fundTransferIn Tran Type=["+tranType+"]");
			
			if ("S0000".equalsIgnoreCase(fundTransferProcessResponse.getCode())) 				
			{
				TxnReqRes txnReqRes = new TxnReqRes();
				txnReqRes.setFundTransferIn(fundTransferIn);
				
				//fundTransferIn.setChannelCode("NLB");
				fundTransferIn.setChannelCode(FundTransferInConstant.FNW_GL);
				if (fundTransferIn.getChannelCode()!=null && fundTransferIn.getChannelCode().trim().length() > 0) 
				{
					fundTransferIn.setChannelCode(fundTransferIn.getChannelCode().trim());
				}				
				fundTransferProcessResponse = loadMasterHandler.processTransactionToLoadBalance(txnReqRes);	
			}
		}
		catch (Exception e) 
		{
			fundTransferProcessResponse.setCode("E0000");
			fundTransferProcessResponse.setStatus("Failed");
			fundTransferProcessResponse.setMessage("Internal Server Error -");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e)); 
		}
		return CompletableFuture.completedFuture(fundTransferIn);
	}
	
	private FundTransferProcessResponse validateFundRequest(FundTransferProcessResponse fundTransferProcessResponse, FundTransferIn fundTransferIn) 
	{
		try 
		{
			fundTransferProcessResponse.setCode("S0000");
			if (fundTransferIn.getRequestId() != null && fundTransferIn.getRequestId().trim().length() > 0) 
			{
				if (fundTransferIn.getSessionId()!=null && fundTransferIn.getSessionId().trim().length() > 0) 
				{
					AccountTranMaster accountTranMaster = new AccountTranMaster();
					accountTranMaster.setStrSys_id(fundTransferIn.getSessionId().trim());
					accountTranMaster.setStrSrcTxnId(fundTransferIn.getRequestId().trim());
					
					AccountTranMaster accountTranResp =	accountTranMasterService.getAccountTranInfoData(accountTranMaster);
					if (accountTranResp!=null && accountTranResp.getStrTxn_id() != null && accountTranResp.getStrTxn_id().trim().length() > 0) 
					{
						fundTransferProcessResponse.setCode("E0000");
						fundTransferProcessResponse.setStatus("Failed");
						fundTransferProcessResponse.setMessage("Duplicate Record");
					}
					
					if ("S0000".equalsIgnoreCase(fundTransferProcessResponse.getCode())) 
					{
						if(fundTransferIn.getSenderAccountNumber()!=null &&fundTransferIn.getSenderAccountNumber().trim().length() > 0) 
						{
							if (fundTransferIn.getBeneficiaryAccountNumber()!=null &&fundTransferIn.getSessionId().trim().length() > 0) 
							{
								if(fundTransferIn.getAmount() == null)
								{
									fundTransferProcessResponse.setCode("E0000");
									fundTransferProcessResponse.setStatus("Failed");
									fundTransferProcessResponse.setMessage("Invalid Request - Amount can not be blank");
								}
								if(fundTransferIn.getAmount() != null && fundTransferIn.getAmount().trim().length() == 0)
								{
									fundTransferProcessResponse.setCode("E0000");
									fundTransferProcessResponse.setStatus("Failed");
									fundTransferProcessResponse.setMessage("Invalid Request - Amount can not be blank");
								}								
							}
							else 
							{
								fundTransferProcessResponse.setCode("E0000");
								fundTransferProcessResponse.setStatus("Failed");
								fundTransferProcessResponse.setMessage("Invalid Request - BeneficiaryAccountNumber can not be blank");
							}
						}
						else 
						{
							fundTransferProcessResponse.setCode("E0000");
							fundTransferProcessResponse.setStatus("Failed");
							fundTransferProcessResponse.setMessage("Invalid Request - SenderAccountNumber can not be blank");
						}
					}
				}
				else 
				{
					fundTransferProcessResponse.setCode("E0000");
					fundTransferProcessResponse.setStatus("Failed");
					fundTransferProcessResponse.setMessage("Invalid Request - SessionId can not be blank");
				}
			}
			else 
			{
				fundTransferProcessResponse.setCode("E0000");
				fundTransferProcessResponse.setStatus("Failed");
				fundTransferProcessResponse.setMessage("Invalid Request - RequestId can not be blank");
			}
		}
		catch (Exception e) 
		{
			fundTransferProcessResponse.setCode("E0000");
			fundTransferProcessResponse.setStatus("Failed");
			fundTransferProcessResponse.setMessage("Internal Server Error --");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e)); 
		} 
		return fundTransferProcessResponse;
	}
}
