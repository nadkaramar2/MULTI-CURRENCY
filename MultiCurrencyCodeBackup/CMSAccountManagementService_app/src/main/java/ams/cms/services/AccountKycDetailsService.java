package ams.cms.services;

import ams.cms.model.AccountKycDetails;

public interface AccountKycDetailsService 
{
	AccountKycDetails saveAccountKycDetails(AccountKycDetails accountKycDetails) throws Exception;
	
	AccountKycDetails getSingleAccountKycDetail(AccountKycDetails accountKycDetails) throws Exception;
	
	int updateKycDetailsInfo(AccountKycDetails accountKycDetails) throws Exception;
	
	int updateKycDetail(AccountKycDetails accountKycDetails) throws Exception;
	
	AccountKycDetails getAccountKycDetails(String mobileNumber) throws Exception;
	
	int saveAddressInformation(AccountKycDetails accountKycDetails);
	
	int saveIdentityInformation(AccountKycDetails accountKycDetails);
	
	int updateKycDetailsInfoCustomerDetails(AccountKycDetails accountKycDetails);
	
	//boolean isKycDocumentAlreadyApplied(AccountKycDetails accountKycDetails) throws Exception;
	
	boolean isDocumentValueAlreadyExist(String documentValue) throws Exception;
}
