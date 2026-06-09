package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountCreditCardTransactionModel;

public interface AccountCreditCardTxnDao extends GenericDao<AccountCreditCardTransactionModel> 
{
	List<AccountCreditCardTransactionModel> getAccountCreditCardTxnWise(AccountCreditCardTransactionModel accountCreditCardTransactionModel);

	List<AccountCreditCardTransactionModel> getCreditCardTxn(AccountCreditCardTransactionModel accountCreditCardTransactionModel);
}
