package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.dao.RevolvingCreditCardDao;
import ams.cms.dao.RevolvingCreditCardMasterDao;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.RevolvingCreditCardMaster;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.services.RevolvingCreditCardService;

@Transactional
@Service
public class RevolvingCreditCardServiceImpl implements RevolvingCreditCardService
{
	@Autowired
	RevolvingCreditCardMasterDao revolvingCreditCardMasterDao;
	
	@Autowired
	RevolvingCreditCardDao revolvingCreditCardDao;
	
	@Override
	public RevolvingCreditCardMaster addRevolvingCreditCardData(RevolvingCreditCardMaster revolvingCreditCardMaster) 
	{
		revolvingCreditCardMasterDao.save(revolvingCreditCardMaster);
		return revolvingCreditCardMaster;
	}

	@Override
	public String getRemainingGracePeriodInDays(RevolvingCreditCardMaster revolvingCreditCardMaster) throws Exception 
	{
		return revolvingCreditCardDao.getRemainingGracePeriodInDays(revolvingCreditCardMaster);
	}
	
	@Override
	public Boolean validateRevolvingCreditCard(RevolvingCreditCardMaster revolvingCreditCardMaster) {
		return revolvingCreditCardMasterDao.validateRevolvingCreditCard(revolvingCreditCardMaster);
	}

	@Override
	public List<AccountCreditBalanceTxnResponse> getRevolvingCreditCardlist(AccountCreditCardTransactionModel accountCreditCardTransactionModel) 
	{
		return revolvingCreditCardMasterDao.getRevolvingCreditCard(accountCreditCardTransactionModel);	
	}

	@Override
	public List<AccountWiseInterestMasterResponse> getRevolvingCreditCardInterestlist(AccountWiseInterestMaster accountWiseInterestMaster) 
	{
		return revolvingCreditCardMasterDao.getRevolvingCreditCard(accountWiseInterestMaster);	

	}
}
