package ams.cms.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.DormantAccountReqResStatusDao;
import ams.cms.model.DormantToActiveMaster;
import ams.cms.services.DormantAccountReqResStatusService;

@Service
public class DormantAccountReqResStatusServiceImpl implements DormantAccountReqResStatusService
{
	@Autowired
	private DormantAccountReqResStatusDao dormantAccountReqResStatusDao;
	
	@Override
	public List<DormantToActiveMaster> getPendingCheckerUserList(DormantToActiveMaster dormantAccountReqResStatus) 
	{
		return dormantAccountReqResStatusDao.getPendingCheckerUserList(dormantAccountReqResStatus);
	}
}
