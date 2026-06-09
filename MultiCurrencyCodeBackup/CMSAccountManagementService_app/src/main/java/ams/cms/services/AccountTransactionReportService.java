package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;

public interface AccountTransactionReportService 
{

	List<AccountTranMaster> getTxnAccountlist(AccountTranMaster accountTranMaster);

	List<TransactionTypeModel> getTxnAccountTypelist(TransactionTypeModel transactionTypeModel);

}
