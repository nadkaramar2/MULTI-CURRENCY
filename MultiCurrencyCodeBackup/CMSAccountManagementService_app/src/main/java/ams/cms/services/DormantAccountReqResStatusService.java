package ams.cms.services;

import java.util.List;

import ams.cms.model.DormantToActiveMaster;

public interface DormantAccountReqResStatusService 
{
	List<DormantToActiveMaster> getPendingCheckerUserList(DormantToActiveMaster dormantAccountReqResStatus);
}
