package ams.cms.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ams.cms.dao.CurrencyTransferMasterDao;
import ams.cms.model.CurrencyTransferMaster;
import ams.cms.services.CurrencyTransferMasterService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class CurrencyTransferMasterServiceImpl  implements CurrencyTransferMasterService{
	
	@Autowired
	CurrencyTransferMasterDao currencyTransferMasterDao;
	
	@Override
	public int addEntryInCurrencyTransferMaster(CurrencyTransferMaster currencyTransferMaster) {
		currencyTransferMaster.setCreatedDate(Utils.getCurrentDate());
		 currencyTransferMasterDao.save(currencyTransferMaster);
		 return 0;
	}

	@Override
	public List<CurrencyTransferMaster> getCurrencyMasterByTranId(CurrencyTransferMaster currencyTransferMaster) {
		// TODO Auto-generated method stub
		return currencyTransferMasterDao.getCurrencyMasterByTranId(currencyTransferMaster);
	}

}
