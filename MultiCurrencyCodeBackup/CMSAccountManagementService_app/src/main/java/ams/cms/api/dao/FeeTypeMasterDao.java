package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.FeeTypeMaster;
import ams.cms.dao.GenericDao;

public interface FeeTypeMasterDao extends GenericDao<FeeTypeMaster>
{
	List<FeeTypeMaster> getGLAccountInfoBasedOnFeeType(FeeTypeMaster feeTypeMaster);
	
	List<FeeTypeMaster> getFeeCollectedBalance(FeeTypeMaster feeTypeMaster);
}
