package ams.cms.services.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.controller.ThirdPartyTransactionController;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.constants.TransactionType;
import ams.cms.dao.GLAccountCreationDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountCreation;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.services.GLAccountCreationService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.txn.handler.AccountTxnMasterHandler;
import ams.cms.txn.handler.ControlAccountTxnHandler;
import ams.cms.utility.Utils;

@Transactional
@Service
public class GLAccountCreationServiceImpl implements GLAccountCreationService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(GLAccountCreationServiceImpl.class);
	
	@Autowired
	GLAccountCreationDao glAccountCreationDao;
	
	@Autowired
	GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired
	AccountTxnMasterHandler accountTxnMasterHandler;
	
	@Autowired
	ControlAccountTxnHandler controlAccountTxnHandler;
	
	@Override
	public GLAccountCreation addGLAccountType(GLAccountCreation glAccountCreation) throws Exception
	{
		glAccountCreation.setCreationDate(new Date());
		glAccountCreationDao.save(glAccountCreation);
		return glAccountCreation;
	}

	@Override
	public boolean isGLAccountTypeAlreadyExist(GLAccountCreation glAccountCreation) 
	{
		return glAccountCreationDao.isGLAccountTypeAlreadyExist(glAccountCreation);
	}

	@Override
	public boolean isGLAccountAccountNumberAlreadyExist(GLAccountCreation glAccountCreation) 
	{
		return glAccountCreationDao.isGLAccountAccountNumberAlreadyExist(glAccountCreation);
	}

	@Override
	public List<GLAccountCreation> getGlAccTypeAccNumber(GLAccountCreation glAccountCreation) {
		return glAccountCreationDao.getGlAccTypeAccNumber(glAccountCreation);
	}
	
	@Override
	public int updateClosingBalance(GLAccountCreation glAccountCreation) 
	{
		try 
		{
			if (glAccountCreation!=null && glAccountCreation.getStrIsThirdPartyTransfer()!=null && "Y".equalsIgnoreCase(glAccountCreation.getStrIsThirdPartyTransfer().trim())) 
			{
				thirdPartyTransferEntries(glAccountCreation);
			}
			
			return glAccountCreationDao.updateClosingBalance(glAccountCreation);
		}
		catch (Exception e) {
		}
		return 0;
	}

	@Override
	public GLAccountCreation isGLAccountTypeExist(GLAccountCreation glAccountCreation) {
		return glAccountCreationDao.isGLAccountTypeExist(glAccountCreation);
	}

	@Override
	public String getAllGLTogetherBalance() 
	{
		return glAccountCreationDao.getAllGLTogetherBalance();
	}
	
	private void thirdPartyTransferEntries(GLAccountCreation glAccountCreation) 
	{
		try 
		{

			String txnId = transactionIdCreationConfigDao.getTransactionId();
			
			GLAccountTypeMaster ctrGLAccountTypeMaster = new GLAccountTypeMaster();
			ctrGLAccountTypeMaster.setStrGLAccountType(glAccountCreation.getStrGLAccountType());
			
			ctrGLAccountTypeMaster = glAccountTypeMasterService.getSingleGLAccountTypeMasterObj(ctrGLAccountTypeMaster);
			
			TransactionConfig ctrlTransactionConfig = new TransactionConfig();
			ctrGLAccountTypeMaster.setStrClosingBalance(glAccountCreation.getStrClosingBalance());
			ctrlTransactionConfig.setGlAccountTypeMaster(ctrGLAccountTypeMaster);			
			ctrlTransactionConfig.setGlTranType(glAccountCreation.getStrTransferTranType());
			
			if ("TIN".equalsIgnoreCase(glAccountCreation.getStrTransferTranType())) 
			{
				ctrlTransactionConfig.setAccountTranType("DPT");
				ctrlTransactionConfig.setGlTranMode(TransactionType.TransactionMode.CREDIT);
			}
			else
			{
				ctrlTransactionConfig.setAccountTranType("WDL");
				ctrlTransactionConfig.setGlTranMode(TransactionType.TransactionMode.DEBIT);
			}
			if (glAccountCreation.getStrParticipantId()!=null ) 
			{
				ctrlTransactionConfig.setParticipantId(glAccountCreation.getStrParticipantId());
			}
			else
			{
				ctrlTransactionConfig.setParticipantId("0");
			}
			ctrlTransactionConfig.setTxnAmount(Utils.decimalFormat.format(Double.valueOf(glAccountCreation.getStrTransferAmount())));
			ctrlTransactionConfig.setAccountNo(ctrGLAccountTypeMaster.getStrAccountNumber());
			ctrlTransactionConfig.setTxnId(txnId);
			
			accountTxnMasterHandler.insertTranMasterEntryWithSingleAccount(ctrlTransactionConfig);// <---- TRAN_MASTER ENTRY OF SINGLE ACCOUNT
			
			if ("TIN".equalsIgnoreCase(glAccountCreation.getStrTransferTranType())) 
			{
				controlAccountTxnHandler.insertControlGLCreditEntryInStatement(ctrlTransactionConfig);
			}
			else
			{
				controlAccountTxnHandler.insertControlGLDebitEntryInStatement(ctrlTransactionConfig);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public List<GLAccountCreation> getGlAccTypeAccNumberList(GLAccountCreation glAccountCreation) 
	{
		return glAccountCreationDao.getGlAccTypeAccNumberList(glAccountCreation);
	}
}
