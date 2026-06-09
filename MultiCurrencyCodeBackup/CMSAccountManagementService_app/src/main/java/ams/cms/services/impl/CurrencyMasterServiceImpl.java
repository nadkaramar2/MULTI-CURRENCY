package ams.cms.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.CurrencyMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CurrencyMaster;
import ams.cms.services.CurrencyMasterService;
import ams.cms.util.ProcessResponse;

@Transactional
@Service
public class CurrencyMasterServiceImpl implements CurrencyMasterService{
	
	private AMSLogger amsLogger = AMSLogger.getInstance(CurrencyMasterServiceImpl.class);
	
	@Autowired
	private CurrencyMasterDao currencyMasterDao;

	@Override
	public CurrencyMaster getCurrencyMasterByCurrencyCode(CurrencyMaster currencyMaster) {
		
		return currencyMasterDao.getCurrencyMasterByCurrencyCode(currencyMaster);
	}

	@Override
	public ProcessResponse getCurrencyList() {
		ProcessResponse processResponse = new ProcessResponse();
		try {
		
		List<CurrencyMaster> currencyMaster = currencyMasterDao.getCurrencyList();
		if(currencyMaster.size()>0) {
		
			//List<String> currencyCode = currencyMaster.stream().map(CurrencyMaster::getCurrencyCode).collect(Collectors.toList());
			List<String> currencyCode = currencyMaster.stream().map(CurrencyMaster::getCurrencyCode).collect(Collectors.toList());
			
			if(currencyCode.size()>0) {
			
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetch Scussefully");
				processResponse.setCurrencyCode(currencyCode);
			}else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Data  Found");
			}
			
		}else {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("No Data  Found");
		}
		
		
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	@Override
	public CurrencyMaster getBaseCurrencyFromCurrencyMaster() {
		
		return currencyMasterDao.getCurrencyMasterByCurrencyCode();
	}

	@Override
	public CurrencyMaster getCurrencyDetailByCurrencyCode(CurrencyMaster currencyMaster) {
		// TODO Auto-generated method stub
		return currencyMasterDao.getCurrencyDetailByCurrencyCode(currencyMaster);
	}
	
	

}
