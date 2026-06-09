package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.CommisisionTypeMasterDao;
import ams.cms.api.model.CommisisionTypeMaster;
import ams.cms.api.service.CommisisionTypeMasterService;
import ams.cms.logger.AMSLogger;

@Transactional
@Service
public class CommisisionTypeMasterServiceImpl implements CommisisionTypeMasterService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CommisisionTypeMasterServiceImpl.class);
	
	@Autowired
	private CommisisionTypeMasterDao commisisionTypeMasterDao;

	@Override
	public List<CommisisionTypeMaster> getGLAccountInfoBasedOnCommisionType(CommisisionTypeMaster commisisionTypeMaster) 
	{
		amsLogger.writeInfoLog("Getting comm and vat gl info.....");
		return commisisionTypeMasterDao.getGLAccountInfoBasedOnCommisionType(commisisionTypeMaster);
	}
}
