package ams.cms.dao;

import java.util.List;

import ams.cms.model.DormantAccountMaster;

public interface DormantMasterDao extends GenericDao<DormantAccountMaster> 
{
	DormantAccountMaster getAccountInformantion(DormantAccountMaster dormantAccountMaster);

	int updateDormantAccountMasterByAccountNo(DormantAccountMaster dormantAccountMasterObj);

	DormantAccountMaster getDormantAccountInformantion(DormantAccountMaster dormantAccountMasterObj);

	DormantAccountMaster getDormantStatusByAccountNumber(DormantAccountMaster dormantAccountMaster);

	int[] batchEntryOfDormantAccount(List<DormantAccountMaster> dormantAccountMaster);
}
