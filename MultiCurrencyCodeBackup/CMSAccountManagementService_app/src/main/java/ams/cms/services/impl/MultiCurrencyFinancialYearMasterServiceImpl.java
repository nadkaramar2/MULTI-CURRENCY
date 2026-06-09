package ams.cms.services.impl;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountMaster;
import ams.cms.dao.MultiCurrencyFinancialYearMasterDao;
import ams.cms.model.AcTypeLrsTcsMaster;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.MultiCurrencyFinancialYearMaster;
import ams.cms.services.AcTypeLrsTcsMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.MultiCurrencyFinancialYearMasterService;
import ams.cms.services.MultiCurrencyWalletAccountService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class MultiCurrencyFinancialYearMasterServiceImpl implements MultiCurrencyFinancialYearMasterService {
	
	@Autowired
	private MultiCurrencyFinancialYearMasterDao multiCurrencyFinancialYearMasterDao;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	

	@Override
	public MultiCurrencyFinancialYearMaster getFinancialYearNyAccountNumber(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		
		return multiCurrencyFinancialYearMasterDao.getFinancialYearNyAccountNumber(multiCurrencyFinancialYearMaster);
	}

	@Override
	public void updatFinancialYearTable(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		multiCurrencyFinancialYearMasterDao.updatFinancialYearTable(multiCurrencyFinancialYearMaster);
		
		
	}

	@Override
	public MultiCurrencyFinancialYearMaster saveMultiCurrencyFinancialYearMaster(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		multiCurrencyFinancialYearMaster.setCreatedDate(Utils.getCurrentSqlDate());
		multiCurrencyFinancialYearMasterDao.save(multiCurrencyFinancialYearMaster);
		return multiCurrencyFinancialYearMaster;
	}

	@Override
	public void resetMultiCurrencyFinancialYearMaster(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		// get All Active Account 
		
		List<AccountMaster> accountMasterActiveList =  accountMasterService.getAllActiveAccount();
		if(accountMasterActiveList.size()>0) {
			accountMasterActiveList.forEach(account -> {
				createFinancialYearMaster(account);
			});
		}
		
	}

	private void createFinancialYearMaster(AccountMaster account) {
		
		String financialYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String nextYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 1);
		
		MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster = new MultiCurrencyFinancialYearMaster();
		multiCurrencyFinancialYearMaster.setAccountNumber(account.getStrAccountNumber());
		multiCurrencyFinancialYearMaster.setAccountType(account.getStrAccountType());
	//	multiCurrencyFinancialYearMaster.setBalance(0);
		multiCurrencyFinancialYearMaster.setCreatedDate(Utils.getCurrentSqlDate());
		multiCurrencyFinancialYearMaster.setFinancialYear(String.valueOf(financialYear.substring(2)+"-"+nextYear.substring(2)));
		multiCurrencyFinancialYearMasterDao.save(multiCurrencyFinancialYearMaster);
	}

	@Override
	public List<MultiCurrencyFinancialYearMaster> getListFinancialYearNyAccountNumberAndChannel(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMasterObj) {
		
		return multiCurrencyFinancialYearMasterDao.getListFinancialYearNyAccountNumberAndChannel(multiCurrencyFinancialYearMasterObj);
	}


	@Override
	public void addMultiCurrencyFinancialYearMaster(MultiCurrencyFinancialYearMaster multiCurrencyFinancialYearMaster) {
		multiCurrencyFinancialYearMaster.setCreatedDate(Utils.getCurrentSqlDate());
		multiCurrencyFinancialYearMasterDao.save(multiCurrencyFinancialYearMaster);
		
	}

	

}
