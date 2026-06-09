package ams.cms.txn.handler;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionConfig;
import ams.cms.handler.impl.AccountLoadMasterHandlerImpl;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.TransactionTypeService;
import ams.cms.utility.Utils;

@Component
public class AccountTxnMasterHandlerImpl implements AccountTxnMasterHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountLoadMasterHandlerImpl.class);
	
	@Autowired 
	AccountTranMasterService accountTranMasterService;
	
	@Autowired
	TransactionTypeService transactionTypeService;

	@Override
	public TransactionConfig insertTranMasterEntryWithSingleAccount(TransactionConfig transactionConfig) 
	{
		try
		{
			AccountTranMaster accountTranMaster = getAccountTranMasterObject(transactionConfig);			
			accountTranMaster.setStrAccountNumber(transactionConfig.getAccountNo());
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				accountTranMaster.setStrSrcTxnId(transactionConfig.getSrcTxnId());
			}
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public TransactionConfig insertTranMasterEntryWithMultipleAccount(TransactionConfig transactionConfig) 
	{
		try
		{
			AccountTranMaster accountTranMaster = getAccountTranMasterObject(transactionConfig);			
			accountTranMaster.setStrFrom_account_number(transactionConfig.getFromAccountNo());
			accountTranMaster.setStrTo_account_number(transactionConfig.getToAccountNo());
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				accountTranMaster.setStrSrcTxnId(transactionConfig.getSrcTxnId());
			}
			accountTranMasterService.addAccountTransactionData(accountTranMaster);
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private AccountTranMaster getAccountTranMasterObject(TransactionConfig transactionConfig) 
	{
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		try 
		{
			accountTranMaster.setStrTxn_id(transactionConfig.getTxnId());
			accountTranMaster.setStrTransaction_amount(transactionConfig.getTxnAmount());
			
			accountTranMaster.setSwitchTxDate(Utils.getCurrentDate());
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			accountTranMaster.setStrTran_type(transactionConfig.getAccountTranType());
			
			try 
			{
				TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
				transactionTypeModel.setStrTxnTypeKeyWord(accountTranMaster.getStrTran_type());
				
				//transactionTypeModel = transactionTypeService.getTransactionTypeMaster(transactionTypeModel);
				transactionTypeModel = transactionTypeService.getTransactionTypeMasterBasedOnTxnType(transactionTypeModel);
				if (transactionTypeModel!=null && transactionTypeModel.getStrID()!=null)
				{
					accountTranMaster.setStrProcessingCode(transactionTypeModel.getStrProcessingCode());
				}
			}
			catch (Exception e) {
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			}
			
			if (transactionConfig.getParticipantId()!=null)
			{
				accountTranMaster.setStrParticipantId(transactionConfig.getParticipantId());
			}
			else
			{
				accountTranMaster.setStrParticipantId("0");
			}
			
			if (transactionConfig.getMcc()!=null) {
				accountTranMaster.setStrMcc(transactionConfig.getMcc());
			}			
			if(transactionConfig.getStrMti()!=null) {
				accountTranMaster.setStrMti(transactionConfig.getStrMti());
			}
			if (transactionConfig.getRrn()!=null) {
				accountTranMaster.setStrRRN(transactionConfig.getRrn());
			}
			if (transactionConfig.getTid()!=null) {
				accountTranMaster.setStrTID(transactionConfig.getTid());
			}
			if (transactionConfig.getMid()!=null) {
				accountTranMaster.setStrMid(transactionConfig.getMid());
			}
			if(transactionConfig.getStan()!=null) {
				accountTranMaster.setStrStan(transactionConfig.getStan());
			}
			if(transactionConfig.getAuthCode()!=null) {
				accountTranMaster.setStrAuthCode(transactionConfig.getAuthCode());
			}
			if(transactionConfig.getResponseCode()!=null) {
				accountTranMaster.setStrResponseCode(transactionConfig.getResponseCode());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTranMaster;
	}

	@Override
	public TransactionConfig updateAccountTranMastersColumn(TransactionConfig transactionConfig) 
	{
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		try 
		{
			amsLogger.writeInfoLog("Inside updateAccountTranMastersColumn transactionConfig in str%%%%%::"+new ObjectMapper().writeValueAsString(transactionConfig));
			
			accountTranMaster.setStrAuthCode(transactionConfig.getAuthCode());
			accountTranMaster.setStrResponseCode(transactionConfig.getResponseCode());
			accountTranMaster.setStrTxn_id(transactionConfig.getTxnId());
			
			accountTranMaster.setStrSys_id(transactionConfig.getSystemTxnId());
			accountTranMaster.setStrSrcTxnId(transactionConfig.getSrcTxnId());
			
			accountTranMaster.setStrReservefield1(transactionConfig.getInformation());
			
			accountTranMasterService.updateAccountTranMasterColumns(accountTranMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
}
