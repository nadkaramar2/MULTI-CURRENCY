package ams.cms.services;

import java.util.List;

import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdTable;
import ams.cms.model.CustomerInfo;

public interface CustomerIDCreationService 
{
	CustomerIdCreation saveAccountInformation(CustomerIdCreation customerIdCreation) throws Exception;

	String getCustomerId() throws Exception;
	
	String getCustId(String year, String julianDate);
	
	int updateCustid(CustomerIdCreation customerIdCreation);
	
	int updateCustIdTable(CustomerIdTable customerIdTable);
	
	int insertCustId(CustomerIdCreation customerIdCreation) ;

	List<CustomerIdCreation> getcustomerdetailsbyId(CustomerIdCreation customerIdCreation);

	CustomerIdCreation getCustomerIdInfo(CustomerIdCreation customerIdCreation) throws Exception;
	
	CustomerIdCreation getCustomerIdInfoByMobile(String mobileNumber) throws Exception;
	
	List<CustomerIdCreation> getCustomerAccountInfo(CustomerIdCreation customerIdCreation);

	List<CustomerIdCreation> getCustomerAccountDetailsBasedOnCustId(CustomerIdCreation customerIdCreation);

	int updateCustomerAccountDetails(CustomerIdCreation customerIdCreation);

	String getActiveTier(String strMobileNo);
	
	CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation);
	
	int updateCustomerAccountDetailsBasedOnParameter(CustomerIdCreation customerIdCreation);
	
	CustomerIdCreation saveCustomerMaster(CustomerIdCreation customerIdCreation);

	int updateCustomerMailId(CustomerIdCreation customerIdCreation);
	
	CustomerIdCreation mapDataToCustomerIdCreation(CustomerInfo customerInfo , String strCustID) throws Exception;
	
	int updateCustomerAccountTier(CustomerIdCreation customerIdCreation);
}
