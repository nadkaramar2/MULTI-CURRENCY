package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.RevolvingCreditCardTxnDao;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.services.RevolvingCreditCardTxnService;

@Transactional
@Service
public class RevolvingCreditCardTxnServiceImpl implements RevolvingCreditCardTxnService
{
	@Autowired
	RevolvingCreditCardTxnDao revolvingCreditCardTxnDao;
	
	@Override
	public RevolvingCreditCardTxnMaster addAccountTransactionData(RevolvingCreditCardTxnMaster revolvingCreditCardTxnMaster)
	{
		revolvingCreditCardTxnMaster.setTxnDate(new Date());
		revolvingCreditCardTxnDao.save(revolvingCreditCardTxnMaster);
		return revolvingCreditCardTxnMaster;
	}
}
