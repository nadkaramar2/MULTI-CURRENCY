package ams.cms.config;

import java.util.List;
import java.util.Map;

import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;

public interface CustomerIdCreationConfigIF {
	
	CustomerIdMap getCustId();
	
	int updateCustId(CustomerIdCreation customerIdCreation);

	List<CustomerIdCreation> getcustomerdetailsbyId(CustomerIdCreation customerIdCreation);

}
