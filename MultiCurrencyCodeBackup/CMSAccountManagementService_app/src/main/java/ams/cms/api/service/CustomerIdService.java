package ams.cms.api.service;

import java.util.List;

import ams.cms.model.CustomerIdCreation;

public interface CustomerIdService {

	String getCustIdExist(CustomerIdCreation customerIdCreation);

	int updateAccountTypeBasedOnMobileNo(CustomerIdCreation customerIdCreation);

	String getIsAccNoCreated(CustomerIdCreation customerIdCreation,String accountType);

	String getMobileNoBasedOnCustId(CustomerIdCreation customerIdCreation);
	
	int updatePinAgainstCustId(CustomerIdCreation customerIdCreation) throws Exception;

	String getCustomerPIN(CustomerIdCreation customerIdCreation);

	String getCustomerMactchlist(CustomerIdCreation customerIdCreation);

	int updateOtpAgainstCustId(CustomerIdCreation customerIdCreation);

	String getCustomerPinOTP(CustomerIdCreation customerIdCreation);

	//added by ankit on 19-04-2023
	List<CustomerIdCreation> getEmailOfCustomer(CustomerIdCreation customerIdCreation);
	//added by ankit on 19-04-2023
	
	//Added by Pankaj Pawar Start
	String getTransactionPinOTP(CustomerIdCreation customerIdCreation);
	//Added by Pankaj Pawar End
	
	CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation);
	
	CustomerIdCreation getCustIdAgentsActiveTier(CustomerIdCreation customerIdCreation);
}
