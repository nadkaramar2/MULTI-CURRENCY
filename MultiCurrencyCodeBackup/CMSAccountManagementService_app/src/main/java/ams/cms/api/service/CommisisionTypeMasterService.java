package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.CommisisionTypeMaster;

public interface CommisisionTypeMasterService 
{
	List<CommisisionTypeMaster> getGLAccountInfoBasedOnCommisionType(CommisisionTypeMaster commisisionTypeMaster);
}
