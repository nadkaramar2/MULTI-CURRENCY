package ams.cms.dao;

import ams.cms.model.CustomerIdTable;

public interface CustomerIdTableDao extends GenericDao<CustomerIdTable> {
	
	int updateCustidtbl(CustomerIdTable customerIdTable);
	

}
