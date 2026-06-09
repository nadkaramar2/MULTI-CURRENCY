package ams.cms.dao;

import ams.cms.model.CloseAccountMaster;

public interface CloseAccountMasterDao extends GenericDao<CloseAccountMaster> {
	
	
	int updateCloseAccountMaster(CloseAccountMaster  CloseAccountMaster);
	
	int updateCloseAccountMasterForRejection(CloseAccountMaster  CloseAccountMaster);
	
	

}
