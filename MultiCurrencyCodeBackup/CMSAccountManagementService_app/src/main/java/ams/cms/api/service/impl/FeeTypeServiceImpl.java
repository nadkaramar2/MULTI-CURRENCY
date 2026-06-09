package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.FeeTypeMasterDao;
import ams.cms.api.model.FeeTypeMaster;
import ams.cms.api.service.FeeTypeService;
import ams.cms.logger.AMSLogger;

@Transactional
@Service
public class FeeTypeServiceImpl implements FeeTypeService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(FeeTypeServiceImpl.class);
	
	@Autowired
	private FeeTypeMasterDao feeTypeMasterDao;

	//@Transactional
	@Override
	public List<FeeTypeMaster> getGLAccountInfoBasedOnFeeType(FeeTypeMaster feeTypeMaster) 
	{
		return feeTypeMasterDao.getGLAccountInfoBasedOnFeeType(feeTypeMaster);
	}

	@Override
	public List<FeeTypeMaster> getFeeCollectedBalance(FeeTypeMaster feeTypeMaster) 
	{
		return feeTypeMasterDao.getFeeCollectedBalance(feeTypeMaster);
	} 
}
