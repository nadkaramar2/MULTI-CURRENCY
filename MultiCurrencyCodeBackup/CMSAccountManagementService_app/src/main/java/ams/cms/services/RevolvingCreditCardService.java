package ams.cms.services;

import java.util.List;

import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.scheduler.model.AccountWiseInterestMaster;

public interface RevolvingCreditCardService 
{
	RevolvingCreditCardMaster addRevolvingCreditCardData(RevolvingCreditCardMaster revolvingCreditCardMaster);
	
	String getRemainingGracePeriodInDays(RevolvingCreditCardMaster revolvingCreditCardMaster) throws Exception;
	
	Boolean validateRevolvingCreditCard(RevolvingCreditCardMaster revolvingCreditCardMaster);

	List<AccountCreditBalanceTxnResponse> getRevolvingCreditCardlist(AccountCreditCardTransactionModel accountCreditCardTransactionModel);

	List<AccountWiseInterestMasterResponse> getRevolvingCreditCardInterestlist(AccountWiseInterestMaster accountWiseInterestMaster);
}
