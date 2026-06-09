package ams.cms.api.handler;

import java.util.HashMap;
import java.util.List;

import ams.cms.api.model.AgencyBankingRequest;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.InflightTransactionMaster;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.util.ProcessResponse;

public interface AgencyBankingHandler 
{
	void processAgentDepositToParkingGL(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);
	
	void processParkingGLDebitUpdate(TransactionPostingConfig transactionPostingConfig, GLAccountTypeMaster gLAccountTypeMaster);
	
	void sendMailToCustomerDepositAtAgentOutlet(TransactionPostingConfig transactionPostingConfig);
	
	void sendMailToCustomerWithdrawalAtAgentOutlet(TransactionPostingConfig transactionPostingConfig);	
	
	HashMap<String, GLAccountTypeMaster> getAgencyBankingResponseMap(List<AgencyBankingResponse> agencyBankingResponseList);
	
	void updateAndAddFeeVatCreditInAgentAccount(InflightTransactionMaster inflightTransactionMaster);
}
