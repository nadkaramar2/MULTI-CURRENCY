package ams.cms.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.MiddleWareBankRequestDao;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.api.service.MiddleWareBankRequestService;
//import ams.cms.logger.AMSLogger;

@Transactional
@Service
public class MiddleWareBankRequestServiceImpl implements MiddleWareBankRequestService 
{
	//private AMSLogger amsLogger = AMSLogger.getInstance(MiddleWareBankRequestServiceImpl.class);
	
	@Autowired 
	private MiddleWareBankRequestDao middleWareBankRequestDao;
	
	@Override
	public MiddleWareBankRequestModel getMiddleWareBankRequestByParticipantId(MiddleWareBankRequestModel middleWareBankRequestModel)
	{
		return middleWareBankRequestDao.getMiddleWareBankRequestByParticipantId(middleWareBankRequestModel);
	}

	@Override
	public int updateMiddleWareAppInfo(MiddleWareBankRequestModel middleWareBankRequestModel) 
	{
		return middleWareBankRequestDao.updateMiddleWareAppInfo(middleWareBankRequestModel);
	}
	
}
