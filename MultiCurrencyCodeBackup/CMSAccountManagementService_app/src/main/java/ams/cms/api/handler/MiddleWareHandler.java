package ams.cms.api.handler;

import java.util.List;

import ams.cms.config.TransactionConfig;
import ams.cms.utility.BankInfoResponseModel;
import ams.cms.utility.MiddleWareBankResponse;

public interface MiddleWareHandler 
{
	List<BankInfoResponseModel> getBankNameList();

	TransactionConfig getNameEnquiryInfo(TransactionConfig transactionConfig);
	
	MiddleWareBankResponse getMiddleWareBankResponse();	
	
	TransactionConfig getPullAccountInformation(TransactionConfig transactionConfig);
}
