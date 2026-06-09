package ams.cms.dao;

import java.util.List;

import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdTable;

public interface CustomerIDCreationDao extends GenericDao<CustomerIdCreation>
{
	CustomerIdCreation getSingleAccountKycDetail(CustomerIdCreation accountKycDetails);
	
	String getCustomerId();
	
	String getCustid(String year, String julianDate);
	
	int updateCustid(CustomerIdCreation customerIdCreation) ;
	
	int updateCustIdTable(CustomerIdTable customerIdTable);

	List<CustomerIdCreation>  getcustomerdetailsbyId(CustomerIdCreation customerIdCreation);
	 
	CustomerIdCreation getCustomerIdInfo(CustomerIdCreation customerIdCreation);
	
	CustomerIdCreation getCustomerIdInfoByMobile(String mobileNumber);
	
	List<CustomerIdCreation> getCustomerAccountInfo(CustomerIdCreation customerIdCreation);	
	
	List<CustomerIdCreation> getCustomerAccountDetailsBasedOnCustId(CustomerIdCreation customerIdCreation);

	int updateCustomerAccountDetails(CustomerIdCreation customerIdCreation);

	String getActiveTier(String strMobileNo);
	
	int updateCustomerAccountDetailsBasedOnParameter(CustomerIdCreation customerIdCreation);
}
