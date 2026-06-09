package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.MultiCurrencyFeeTypeMasterDao;
import ams.cms.model.MultiCurrencyFeeTypeMaster;
import ams.cms.services.FeeTypeMasterService;
import ams.cms.services.MultiCurrencyFeeTypeMasterService;

@Transactional
@Service
public class MultiCurrencyFeeTypeMasterServiceImpl implements MultiCurrencyFeeTypeMasterService{
	
	@Autowired
	private MultiCurrencyFeeTypeMasterDao feeTypeMasterDao;

	@Override
	public MultiCurrencyFeeTypeMaster getFeeTypeDetails(MultiCurrencyFeeTypeMaster feeTypeMaster) {
		
		return feeTypeMasterDao.getFeeTypeDetails(feeTypeMaster);
	}

}
