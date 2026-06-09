package ams.cms.dao;

import ams.cms.model.RevolvingCreditCardMaster;

public interface RevolvingCreditCardDao extends GenericDao<RevolvingCreditCardMaster>
{
	String getGracePeriod(String accountType) ;
	
	String getRemainingGracePeriodInDays(RevolvingCreditCardMaster revolvingCreditCardMaster);
	
	//String getRemainingGracePeriodInDays(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster);
}
