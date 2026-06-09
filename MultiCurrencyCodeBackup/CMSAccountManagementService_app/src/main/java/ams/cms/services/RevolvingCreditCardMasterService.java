package ams.cms.services;

import ams.cms.model.RevolvingCreditCardTxnMaster;
public interface RevolvingCreditCardMasterService {
	
	String getGracePeriod(String accountType);
	
	//String getRemainingGracePeriodInDays(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster);
}
