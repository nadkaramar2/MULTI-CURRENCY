package ams.cms.txn.handler;

import ams.cms.config.TransactionConfig;

public interface ExternalServerHandler 
{
	void addBankDetailsFromNIBSS(String data);
	
	TransactionConfig getBeneficiaryInfo(TransactionConfig transactionConfig);
	
	TransactionConfig sendOtpSms(TransactionConfig transactionConfig);
}
