package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.FeeTypeMaster;

public interface FeeTypeService 
{
	List<FeeTypeMaster> getGLAccountInfoBasedOnFeeType(FeeTypeMaster feeTypeMaster);
	
	List<FeeTypeMaster> getFeeCollectedBalance(FeeTypeMaster feeTypeMaster);
}
