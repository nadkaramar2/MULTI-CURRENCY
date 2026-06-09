package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.LoadMoneyTxnDetailsDao;
import ams.cms.model.LoadMoneyTxnDetails;
import ams.cms.services.LoadMoneyTxnDetailsService;

@Service
@Transactional
public class LoadMoneyTxnDetailsServiceImpl implements LoadMoneyTxnDetailsService{
	
	@Autowired
	private LoadMoneyTxnDetailsDao loadMoneyTxnDetailsDao;

	
	@Override
	public LoadMoneyTxnDetails getLoadDetailTxnByAccountNumber(LoadMoneyTxnDetails loadMoneyTxnDetails) {
		
		return loadMoneyTxnDetailsDao.getLoadDetailTxnByAccountNumber(loadMoneyTxnDetails);
	}

	@Override
	public void saveLoadMoney(LoadMoneyTxnDetails loadMoneyTxnDetails) {
		loadMoneyTxnDetails.setLoadedDate(new Date());
		loadMoneyTxnDetailsDao.save(loadMoneyTxnDetails);
		
	}

}
