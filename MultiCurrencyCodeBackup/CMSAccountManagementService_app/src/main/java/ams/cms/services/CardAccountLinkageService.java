package ams.cms.services;

import java.util.List;

import ams.cms.api.model.CardAccountLinkageResponse;
import ams.cms.model.CardAccountLinkage;

public interface CardAccountLinkageService 
{
	List<CardAccountLinkage> getCardAccountLinkageBasedOnAccount(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	List<CardAccountLinkage> getCardAccountLinkageDataBasedOnCard(CardAccountLinkage cardAccountLinkage) throws Exception; 
	
	CardAccountLinkage addCardAccountLinkageData(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	boolean isCardAccountLinkageDataExist(CardAccountLinkage cardAccountLinkage) throws Exception;

	List<CardAccountLinkageResponse> getLinkagecardlist(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	CardAccountLinkage getCardAccountLinkages(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	List<CardAccountLinkage> getCardLinkAccountList(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	List<CardAccountLinkage> getLinkageCardDetailsBasedOnCustId(CardAccountLinkage cardAccountLinkage) throws Exception;
	
	CardAccountLinkage getLinkageCardDetailsOnCustId(CardAccountLinkage cardAccountLinkage) throws Exception;

	int updateCardStatusBlock(CardAccountLinkage cardAccountLinkage);

	int updateCardStatusUnblock(CardAccountLinkage cardAccountLinkage);
	
	int updateExpiryDate(CardAccountLinkage cardAccountLinkage);
}
