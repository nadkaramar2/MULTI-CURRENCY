package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.RevolvingCreditInterestTxnDao;
import ams.cms.model.RevolvingCreditCardTxnMaster;
import ams.cms.model.RevolvingCreditInterestTxn;
import ams.cms.services.RevolvingCreditInterestTxnService;

@Transactional
@Service
public class RevolvingCreditInterestTxnServiceImpl implements RevolvingCreditInterestTxnService{
	
	@Autowired
	RevolvingCreditInterestTxnDao revolvingCreditInterestTxnDao;
	
	@Override
	public RevolvingCreditInterestTxn addAccountTransactionData(
			RevolvingCreditInterestTxn revolvingCreditInterestTxn) {
		revolvingCreditInterestTxnDao.save(revolvingCreditInterestTxn);
		return null;
	}

	
	

}
