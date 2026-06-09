package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;

public interface AccountTransactionReportDao extends GenericDao<AccountTranMaster>
{

	List<AccountTranMaster> getTxnAccountlist(AccountTranMaster accountTranMaster);

	List<TransactionTypeModel> getTxnAccountTypelist(TransactionTypeModel transactionTypeModel);

}
