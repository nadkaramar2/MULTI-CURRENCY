package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.RevolvingCreditCardDao;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.services.RevolvingCreditCardMasterService;

@Transactional
@Service
public class RevolvingCreditCardMasterServiceImpl implements RevolvingCreditCardMasterService {
	
	@Autowired
	RevolvingCreditCardDao revolvingCreditCardDao;
	
	@Override
	public String getGracePeriod(String accountType) {
	
		return revolvingCreditCardDao.getGracePeriod(accountType);
	}
	
	/*
	@Override
	public String getRemainingGracePeriodInDays(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster)
	{
		return revolvingCreditCardDao.getRemainingGracePeriodInDays(revolvingCreditCardTxnMaster);
	}
	*/
}
