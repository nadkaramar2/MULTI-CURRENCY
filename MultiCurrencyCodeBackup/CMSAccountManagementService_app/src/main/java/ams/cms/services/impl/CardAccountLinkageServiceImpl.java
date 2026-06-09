package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.CardAccountLinkageResponse;
import ams.cms.api.utitlity.Utils;
import ams.cms.dao.CardAccountLinkageDao;
import ams.cms.model.CardAccountLinkage;
import ams.cms.services.CardAccountLinkageService;

@Transactional
@Service
public class CardAccountLinkageServiceImpl implements CardAccountLinkageService
{
	@Autowired
	CardAccountLinkageDao cardAccountLinkageDao;

	@Override
	public List<CardAccountLinkage> getCardAccountLinkageBasedOnAccount(CardAccountLinkage cardAccountLinkage) throws Exception {
		return cardAccountLinkageDao.getCardAccountLinkageBasedOnAccount(cardAccountLinkage);
	}

	@Override
	public List<CardAccountLinkage> getCardAccountLinkageDataBasedOnCard(CardAccountLinkage cardAccountLinkage) throws Exception {
		return cardAccountLinkageDao.getCardAccountLinkageDataBasedOnCard(cardAccountLinkage);
	}

	@Override
	public CardAccountLinkage addCardAccountLinkageData(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		cardAccountLinkage.setCreationDate(new Date());
		cardAccountLinkageDao.save(cardAccountLinkage);
		return cardAccountLinkage;
	}

	@Override
	public boolean isCardAccountLinkageDataExist(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		return cardAccountLinkageDao.isCardAccountLinkageDataExist(cardAccountLinkage);
	}

	@Override
	public List<CardAccountLinkageResponse> getLinkagecardlist(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		 List<CardAccountLinkage> linkageCardlist = cardAccountLinkageDao.getLinkagecardlist(cardAccountLinkage);
		 List<CardAccountLinkageResponse> cardAccountLinkageResponseList = new ArrayList<>();
		 
		 if (linkageCardlist!=null && linkageCardlist.size() > 0) 
		 {
			 for(int i=0;i<linkageCardlist.size();i++) 
			 {
				 CardAccountLinkage	cardLinkage = linkageCardlist.get(i);
				 CardAccountLinkageResponse mapcardLinkageToCardAccountLinkageResponse= mapcardLinkageToCardAccountLinkageResponse(cardLinkage);
				 cardAccountLinkageResponseList.add(mapcardLinkageToCardAccountLinkageResponse);
			 }
		 }
		 return cardAccountLinkageResponseList;
	}

	private CardAccountLinkageResponse mapcardLinkageToCardAccountLinkageResponse(CardAccountLinkage cardLinkage) 
	{
		CardAccountLinkageResponse cardAccountLinkageResponse=new CardAccountLinkageResponse();
		cardAccountLinkageResponse.setStrParticipantId(cardLinkage.getStrParticipantID());
		cardAccountLinkageResponse.setStrAccountType(cardLinkage.getStrAccountType());
		cardAccountLinkageResponse.setStrAccountNumber(cardLinkage.getStrAccountNumber());
		cardAccountLinkageResponse.setStrCardNumber(cardLinkage.getStrCardNumber());
		
		cardAccountLinkageResponse.setStrCardId(cardLinkage.getStrCardId());
		cardAccountLinkageResponse.setStrCardType(cardLinkage.getStrCardType());
		cardAccountLinkageResponse.setStrCardDescription(cardLinkage.getStrCardDescription());
		
		cardAccountLinkageResponse.setStrCardStatus(cardLinkage.getStrCardStatus());
		cardAccountLinkageResponse.setStrAccountStatus(cardLinkage.getStrAccountStatus());
		cardAccountLinkageResponse.setStrCardHolderName(cardLinkage.getStrCardHolderName());
		cardAccountLinkageResponse.setStrNetworkType(cardLinkage.getStrNetworkType());
		cardAccountLinkageResponse.setStrCardExpDate(	Utils.getExpiryDateStr(	cardLinkage.getStrCardExpDate()));
		cardAccountLinkageResponse.setStrCardCvv(cardLinkage.getStrCardCvv());
		cardAccountLinkageResponse.setCreation_date(cardLinkage.getCreationDate());
		cardAccountLinkageResponse.setStrTokenCard(cardLinkage.getStrTokenCard());
		
		return cardAccountLinkageResponse;
	}

	@Override
	public CardAccountLinkage getCardAccountLinkages(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		return cardAccountLinkageDao.getCardAccountLinkages(cardAccountLinkage);
	}

	@Override
	public List<CardAccountLinkage> getCardLinkAccountList(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		return cardAccountLinkageDao.getCardLinkAccountList(cardAccountLinkage);
	}

	@Override
	public List<CardAccountLinkage> getLinkageCardDetailsBasedOnCustId(CardAccountLinkage cardAccountLinkage) throws Exception 
	{
		return cardAccountLinkageDao.getLinkageCardDetailsBasedOnCustId(cardAccountLinkage);
	}

	@Override
	public CardAccountLinkage getLinkageCardDetailsOnCustId(CardAccountLinkage cardAccountLinkage) throws Exception {
		
		return cardAccountLinkageDao.getLinkageCardDetailsOnCustId(cardAccountLinkage);
	}

	@Override
	public int updateCardStatusBlock(CardAccountLinkage cardAccountLinkage) {
		return cardAccountLinkageDao.updateCardStatusBlock(cardAccountLinkage);
	}

	@Override
	public int updateCardStatusUnblock(CardAccountLinkage cardAccountLinkage) {
		return  cardAccountLinkageDao.updateCardStatusUnBlock(cardAccountLinkage);
	}
	
	@Override
	public int updateExpiryDate(CardAccountLinkage cardAccountLinkage) {
		return cardAccountLinkageDao.updateExpiryDate(cardAccountLinkage);
	}

}
