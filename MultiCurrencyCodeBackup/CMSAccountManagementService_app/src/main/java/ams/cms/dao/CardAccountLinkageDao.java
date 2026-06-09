package ams.cms.dao;

import java.util.List;

import ams.cms.model.CardAccountLinkage;

public interface CardAccountLinkageDao extends GenericDao<CardAccountLinkage>
{
	List<CardAccountLinkage> getCardAccountLinkageBasedOnAccount(CardAccountLinkage cardAccountLinkage);
	
	List<CardAccountLinkage> getCardAccountLinkageDataBasedOnCard(CardAccountLinkage cardAccountLinkage);
	
	boolean isCardAccountLinkageDataExist(CardAccountLinkage cardAccountLinkage);

	List<CardAccountLinkage> getAccountCardLinkagelist(CardAccountLinkage cardAccountLinkage);

	List<CardAccountLinkage> getCardAccountlinkagelist(CardAccountLinkage cardAccountLinkage);

	List<CardAccountLinkage> getLinkagecardlist(CardAccountLinkage cardAccountLinkage);
	
	CardAccountLinkage getCardAccountLinkages(CardAccountLinkage cardAccountLinkage);
	
	List<CardAccountLinkage> getCardLinkAccountList(CardAccountLinkage cardAccountLinkage);
	
	List<CardAccountLinkage> getLinkageCardDetailsBasedOnCustId(CardAccountLinkage cardAccountLinkage);
	
	CardAccountLinkage getLinkageCardDetailsOnCustId(CardAccountLinkage cardAccountLinkage) ;

	int updateCardStatusBlock(CardAccountLinkage cardAccountLinkage);
	
	int updateCardStatusUnBlock(CardAccountLinkage cardAccountLinkage);

	int updateExpiryDate(CardAccountLinkage cardAccountLinkage);
}
