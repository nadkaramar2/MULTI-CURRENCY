package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.model.AccountTranMaster;

public interface AccountTranMasterDao extends GenericDao<AccountTranMaster>
{
	List<AccountTranMaster> getAccountTransactionlist(AccountTranMaster accountTranMaster);

	List<AccountTranMaster> getAcctTransactionlist(AccountTranMaster accountTranMaster);
	
	AccountTranMaster getAccountInfo(AccountTranMaster accountTranMaster);
	
	//created by ankit on 16-04-2023
	List<AccountTranMaster> getTransactionIfExist(AccountTranMaster accountTranMaster);
	//created by ankit on 16-04-2023
	
	List<AccountTranMaster> getTxnAccountTypelist(AccountTranMaster accountTranMaster);
	
	int updateAccountTranMasterColumns(AccountTranMaster accountTranMaster);
	
	List<AccountTranMaster> getTransactionByTxnId(AccountTranMaster accountTranMaster);
	
	int[] batchEntryOfAccountTranMaster(List<AccountTranMaster> accountTranMasters);
	
	int updateAccountTranMasterForSendMoney(AccountTranMaster accountTranMaster);
	
	AccountTranMaster getAccountTranMaster(AccountTranMaster accountTranMaster);
	
	List<AgencyBankingResponse> getAccountTranMasterWithAccountInfo(AccountTranMaster accountTranMaster);
	
	AccountTranMaster getAccountTranInfoData(AccountTranMaster accountTranMaster);
}
