package ams.cms.api.dao;

import java.util.List;

import ams.cms.model.CustomerIdCreation;

public interface CustomerIdDao 
{
	String getCustIdExist(CustomerIdCreation customerIdCreation);
	
	int updateAccountTypeBasedOnMobileNo(CustomerIdCreation customerIdCreation);

	String getIsAccNoCreated(CustomerIdCreation customerIdCreation, String accountType);

	String getMobileNoBasedOnCustId(CustomerIdCreation customerIdCreation);
	
	List<CustomerIdCreation> getCustomerPinVerifly(CustomerIdCreation customerIdCreation);

	int updatePinAgainstCustId(CustomerIdCreation customerIdCreation);

	String getCustomerPIN(CustomerIdCreation customerIdCreation);

	String getCustomerMactchlist(CustomerIdCreation customerIdCreation);
	
	int updateOtpAgainstCustId(CustomerIdCreation customerIdCreation);

	String getCustomerPinOTP(CustomerIdCreation customerIdCreation);
	
	//created by ankit on 19-04-2023
	List<CustomerIdCreation> getEmailOfCustomer(CustomerIdCreation customerIdCreation);
	//created by ankit on 19-04-2023
	
	//Added by Pankaj Pawar Start
	String getTransactionPinOTP(CustomerIdCreation customerIdCreation);
	//Added by Pankaj Pawar End
	
	CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation);
	
	CustomerIdCreation getCustIdAgentsActiveTier(CustomerIdCreation customerIdCreation);
}
