package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.TransactionCurrencyMasterDao;
import ams.cms.model.TransactionCurrencyMaster;
import ams.cms.services.TransactionCurrencyMasterService;

@Service
@Transactional
public class TransactionCurrencyMasterServiceImpl implements TransactionCurrencyMasterService {
	
	@Autowired
	private TransactionCurrencyMasterDao transactionCurrencyMasterDao;

	@Override
	public void saveTransactionCurrencyMaster(TransactionCurrencyMaster transactionCurrencyMasterService) {
		transactionCurrencyMasterDao.save(transactionCurrencyMasterService);
		
	}

	

}
