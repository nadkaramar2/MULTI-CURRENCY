package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountTranMasterResponse;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.RefundRequest;
import ams.cms.api.model.ReverseTransactionRequest;
import ams.cms.model.AccountTranMaster;

public interface AccountTranMasterService 
{
	AccountTranMaster addAccountTransactionData(AccountTranMaster accountTranMaster);

	List<AccountTranMasterResponse> getAccountTransactionlist(AccountTranMaster accountTranMaster);

	List<AccountTranMasterResponse> getAcctTransactionlist(AccountTranMaster accountTranMaster);
	
	AccountTranMaster getAccountInfo(AccountTranMaster accountTranMaster) throws Exception;
	
	//created by ankit on 16-04-2023
	List<AccountTranMaster> verifyTransactionForRefund(RefundRequest refundRequest);

	List<AccountTranMaster> verifyTransactionForReversal(ReverseTransactionRequest refundTransactionRequest);
	//created by ankit on 16-04-2023
	
	List<AccountTranMaster> getTxnAccountTypelist(AccountTranMaster accountTranMaster);
	
	AccountTranMaster addEntryInAccountTranMasterData(AccountTranMaster accountTranMaster);
	
	int updateAccountTranMasterColumns(AccountTranMaster accountTranMaster);
	
	List<AccountTranMaster> getTransactionByTxnId(AccountTranMaster accountTranMaster);
	
	int[] batchEntryOfAccountTranMaster(List<AccountTranMaster> accountTranMasters) throws Exception;
	
	AccountTranMaster getAccountTranMaster(AccountTranMaster accountTranMaster) throws Exception;
	
	List<AgencyBankingResponse> getAccountTranMasterWithAccountInfo(AccountTranMaster accountTranMaster);
	
	AccountTranMaster getAccountTranInfoData(AccountTranMaster accountTranMaster) throws Exception;
}
