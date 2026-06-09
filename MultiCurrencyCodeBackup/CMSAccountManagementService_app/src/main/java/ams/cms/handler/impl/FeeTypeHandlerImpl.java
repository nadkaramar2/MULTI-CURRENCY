package ams.cms.handler.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.service.FeeTypeService;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.handler.FeeTypeHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.GLAccountTypeMaster;

@Component
public class FeeTypeHandlerImpl implements FeeTypeHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(FeeTypeHandlerImpl.class);
	
	@Autowired
	private FeeTypeService feeTypeService;
	
	@Override
	public TransactionPostingConfig getFeeAndVatGLAccounts(TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			FeeTypeMaster feeTypeMaster = new FeeTypeMaster();
			feeTypeMaster.setFeeType(transactionPostingConfig.getFeeType().trim());
			
			List<FeeTypeMaster> feeTypeMasters = feeTypeService.getGLAccountInfoBasedOnFeeType(feeTypeMaster);
			if (feeTypeMasters != null && feeTypeMasters.size() > 0) 
			{
				GLAccountTypeMaster feeGLAccountType = new GLAccountTypeMaster();
				GLAccountTypeMaster vatGLAccountType = new GLAccountTypeMaster();
				if (feeTypeMasters.get(0).getFeeType() != null && feeTypeMasters.get(0).getFeeType().trim().length() > 0)
				{
					
					feeGLAccountType.setStrAccountNumber(feeTypeMasters.get(0).getGlAccountNo());
					feeGLAccountType.setStrGLAccountType(feeTypeMasters.get(0).getGlAccountType());
					feeGLAccountType.setStrClosingBalance(feeTypeMasters.get(0).getGlAccountBalance());
					feeGLAccountType.setStrGLAccountDescription(feeTypeMasters.get(0).getGlAccountRef());
				}
				else
				{
					feeGLAccountType.setStrAccountNumber(feeTypeMasters.get(1).getGlAccountNo());
					feeGLAccountType.setStrGLAccountType(feeTypeMasters.get(1).getGlAccountType());
					feeGLAccountType.setStrClosingBalance(feeTypeMasters.get(1).getGlAccountBalance());
					feeGLAccountType.setStrGLAccountDescription(feeTypeMasters.get(1).getGlAccountRef());
				}
				if (feeTypeMasters.get(0).getVatType() != null && feeTypeMasters.get(0).getVatType().trim().length() > 0)
				{
					vatGLAccountType.setStrAccountNumber(feeTypeMasters.get(0).getGlAccountNo());
					vatGLAccountType.setStrGLAccountType(feeTypeMasters.get(0).getGlAccountType());
					vatGLAccountType.setStrClosingBalance(feeTypeMasters.get(0).getGlAccountBalance());
					vatGLAccountType.setStrGLAccountDescription(feeTypeMasters.get(0).getGlAccountRef());
				}
				else
				{
					vatGLAccountType.setStrAccountNumber(feeTypeMasters.get(1).getGlAccountNo());
					vatGLAccountType.setStrGLAccountType(feeTypeMasters.get(1).getGlAccountType());
					vatGLAccountType.setStrClosingBalance(feeTypeMasters.get(1).getGlAccountBalance());
					vatGLAccountType.setStrGLAccountDescription(feeTypeMasters.get(1).getGlAccountRef());
				}
				transactionPostingConfig.setFeeGLAccount(feeGLAccountType); 
				transactionPostingConfig.setVatGLAccount(vatGLAccountType);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionPostingConfig;
	}
	
}
