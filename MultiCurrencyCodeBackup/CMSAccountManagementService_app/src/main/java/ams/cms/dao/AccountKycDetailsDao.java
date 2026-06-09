package ams.cms.dao;

import ams.cms.model.AccountKycDetails;

public interface AccountKycDetailsDao extends GenericDao<AccountKycDetails>
{
	AccountKycDetails getSingleAccountKycDetail(AccountKycDetails accountKycDetails);
	
	AccountKycDetails getAccountKycDetails(String mobileNo);
	
	int updateKycDetailsInfo(AccountKycDetails accountKycDetails);
	
	int updateKycDetail(AccountKycDetails accountKycDetails);
	
	// added by ankit
	int updateAddressInformation(AccountKycDetails accountKycDetails);

	int updateIdentityInformation(AccountKycDetails accountKycDetails);
	// added by ankit
	
	int updateKycDetailsInfoCustomerDetails(AccountKycDetails accountKycDetails);
	
	boolean isDocumentValueAlreadyExist(String documentValue);
	//boolean isKycDocumentAlreadyApplied(AccountKycDetails accountKycDetails);
	
}
