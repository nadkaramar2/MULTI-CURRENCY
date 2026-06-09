package ams.cms.dao;

import ams.cms.model.AccountTransactionLimitation;

public interface AccountTransactionLimitationDao extends GenericDao<AccountTransactionLimitation> 
{
	AccountTransactionLimitation getAccountTxnLimitBasedOnParam(AccountTransactionLimitation accountTransactionLimitation);
}
