package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.scheduler.model.AccountWiseInterestMaster;

public interface RevolvingCreditCardMasterDao extends GenericDao<RevolvingCreditCardMaster> 
{
	Boolean validateRevolvingCreditCard(RevolvingCreditCardMaster revolvingCreditCardMaster);

	List<AccountCreditBalanceTxnResponse> getRevolvingCreditCard(AccountCreditCardTransactionModel accountCreditCardTransactionModel);

	List<AccountWiseInterestMasterResponse> getRevolvingCreditCard(AccountWiseInterestMaster accountWiseInterestMaster);
}
