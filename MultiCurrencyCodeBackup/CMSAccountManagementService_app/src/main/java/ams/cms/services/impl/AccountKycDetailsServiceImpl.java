package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountKycDetailsDao;
import ams.cms.model.AccountKycDetails;
import ams.cms.services.AccountKycDetailsService;

@Transactional
@Service
public class AccountKycDetailsServiceImpl implements AccountKycDetailsService
{
	@Autowired
	AccountKycDetailsDao accountKycDetailsDao;
	
	@Override
	public AccountKycDetails saveAccountKycDetails(AccountKycDetails accountKycDetails) throws Exception 
	{
		accountKycDetailsDao.save(accountKycDetails);
		return accountKycDetails;
	}
	
	@Override
	public AccountKycDetails getSingleAccountKycDetail(AccountKycDetails accountKycDetails) throws Exception 
	{
		return accountKycDetailsDao.getSingleAccountKycDetail(accountKycDetails);
	}
	
	@Override
	public int updateKycDetailsInfo(AccountKycDetails accountKycDetails) throws Exception {
		return accountKycDetailsDao.updateKycDetailsInfo(accountKycDetails);
	}
	
	
	@Override
	public int updateKycDetail(AccountKycDetails accountKycDetails) throws Exception {
		return accountKycDetailsDao.updateKycDetail(accountKycDetails);
	}
	
	@Override
	public AccountKycDetails getAccountKycDetails(String mobileNo) {
		return accountKycDetailsDao.getAccountKycDetails(mobileNo);
	}
	
	@Override
	public int saveAddressInformation(AccountKycDetails accountKycDetails) {
		int updateAddressInformation = accountKycDetailsDao.updateAddressInformation(accountKycDetails);
		return updateAddressInformation;
	}

	@Override
	public int saveIdentityInformation(AccountKycDetails accountKycDetails) {
		int updateAddressInformation = accountKycDetailsDao.updateIdentityInformation(accountKycDetails);
		return updateAddressInformation;
	}

	@Override
	public int updateKycDetailsInfoCustomerDetails(AccountKycDetails accountKycDetails) {
		return accountKycDetailsDao.updateKycDetailsInfoCustomerDetails(accountKycDetails);
	}

	@Override
	public boolean isDocumentValueAlreadyExist(String documentValue) throws Exception 
	{
		return accountKycDetailsDao.isDocumentValueAlreadyExist(documentValue);
	}
}
