package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.CustomerIdDao;
import ams.cms.api.service.CustomerIdService;
import ams.cms.model.CustomerIdCreation;

@Transactional
@Service
public class CustomerIdServiceImpl implements CustomerIdService{

	@Autowired
	CustomerIdDao customerIdDao;
	
	@Override
	public String getCustIdExist(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustIdExist(customerIdCreation);
	}
	
	@Override
	public int updateAccountTypeBasedOnMobileNo(CustomerIdCreation customerIdCreation) {
		return customerIdDao.updateAccountTypeBasedOnMobileNo(customerIdCreation);
	}

	@Override
	public String getIsAccNoCreated(CustomerIdCreation customerIdCreation,String accountType) {
		return customerIdDao.getIsAccNoCreated(customerIdCreation,accountType);
	}

	@Override
	public String getMobileNoBasedOnCustId(CustomerIdCreation customerIdCreation) 
	{	
		return customerIdDao.getMobileNoBasedOnCustId(customerIdCreation);
	}


	@Override
	public int updatePinAgainstCustId(CustomerIdCreation customerIdCreation) throws Exception {
		return customerIdDao.updatePinAgainstCustId(customerIdCreation);
	}


	@Override
	public String getCustomerPIN(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustomerPIN(customerIdCreation);
	}


	@Override
	public String getCustomerMactchlist(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustomerMactchlist(customerIdCreation);
	}


	@Override
	public int updateOtpAgainstCustId(CustomerIdCreation customerIdCreation) {
		return customerIdDao.updateOtpAgainstCustId(customerIdCreation);
	}


	@Override
	public String getCustomerPinOTP(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustomerPinOTP(customerIdCreation);
	}

	//created by ankit on 19-04-2023
	@Override
	public List<CustomerIdCreation> getEmailOfCustomer(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getEmailOfCustomer(customerIdCreation);
	}
	//created by ankit on 19-04-2023
	
	//Added By Pankaj Pawar Start
	@Override
	public String getTransactionPinOTP(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getTransactionPinOTP(customerIdCreation);
	}
	//Added By Pankaj Pawar End

	@Override
	public CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustomerInformationByCustId(customerIdCreation);
	}
	
	public CustomerIdCreation getCustIdAgentsActiveTier(CustomerIdCreation customerIdCreation) 
	{
		return customerIdDao.getCustIdAgentsActiveTier(customerIdCreation);
	}
}
