package ams.cms.dao;

import java.util.List;


import ams.cms.model.DormantToActiveMaster;

public interface DormantAccountReqResStatusDao extends GenericDao<DormantToActiveMaster>
{
	List<DormantToActiveMaster> getPendingCheckerUserList(DormantToActiveMaster dormantToActiveMaster);

	DormantToActiveMaster findDormantToActiveRequest(DormantToActiveMaster dormantToActiveMaster);

	int updateDormantToActiveByAccountNumber(DormantToActiveMaster dormantToActiveMaster);

	int updateDormantToActiveByAccountNumberForReject(DormantToActiveMaster dormantToActiveMaster);
}
