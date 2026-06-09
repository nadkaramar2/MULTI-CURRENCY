package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.MultiCurrencyWalletAccountTypeDao;
import ams.cms.model.MultiCurrencyWalletAccountTypeMaster;
import ams.cms.services.MultiCurrencyWalletAccountTypeService;

@Transactional
@Service
public class MultiCurrencyWalletAccountTypeServiceImpl implements MultiCurrencyWalletAccountTypeService
{
	@Autowired
	MultiCurrencyWalletAccountTypeDao multiCurrencyWalletAccountTypeDao;
	
	@Override
	public MultiCurrencyWalletAccountTypeMaster insertEntryForAccountWisePriorityWallet(MultiCurrencyWalletAccountTypeMaster multiCurrencyWalletAccountTypeMaster) {
		
		  multiCurrencyWalletAccountTypeDao.save(multiCurrencyWalletAccountTypeMaster);
		  return multiCurrencyWalletAccountTypeMaster;
	}

	@Override
	public List<MultiCurrencyWalletAccountTypeMaster> getAccountTypeWiseMultiCurrencyWalletList(MultiCurrencyWalletAccountTypeMaster multiCurrencyWalletAccountTypeMaster) 
	{
		return multiCurrencyWalletAccountTypeDao.getAccountTypeWiseMultiCurrencyWalletList(multiCurrencyWalletAccountTypeMaster);
	}

	@Override
	public MultiCurrencyWalletAccountTypeMaster getMultiCurrencyAccountTypeMasterByCurrencyCode(MultiCurrencyWalletAccountTypeMaster currnecyWalletAccount) {
		return multiCurrencyWalletAccountTypeDao.getMultiCurrencyAccountTypeMasterByCurrencyCode(currnecyWalletAccount);	}

	@Override
	public MultiCurrencyWalletAccountTypeMaster getMultiCurrencyAccountTypeMasterByCurrencyCodeAndAccountType(MultiCurrencyWalletAccountTypeMaster multiCurrencyWalletAccountTypeMaster) {
		
		return multiCurrencyWalletAccountTypeDao.getMultiCurrencyAccountTypeMasterByCurrencyCodeAndAccountType(multiCurrencyWalletAccountTypeMaster);
	}

}
