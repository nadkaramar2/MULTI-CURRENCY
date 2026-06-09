package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.CurrencyConversionMasterDao;
import ams.cms.model.CurrencyConversionMaster;
import ams.cms.services.CurrencyConversionMasterService;

@Transactional
@Service
public class CurrencyConversionMasterServiceImpl implements CurrencyConversionMasterService {
	
	@Autowired
	private CurrencyConversionMasterDao conversionMasterDao;

	@Override
	public CurrencyConversionMaster getCurrencyConverionValue(CurrencyConversionMaster currencyConversionMaster) {
		
		return conversionMasterDao.getCurrencyConverionValue(currencyConversionMaster);
	}

	@Override
	public CurrencyConversionMaster getCurrencyMasterByCurrencyCode(CurrencyConversionMaster currencyMaster) {
		// TODO Auto-generated method stub
		return conversionMasterDao.getCurrencyMasterByCurrencyCode(currencyMaster);
	}

}
