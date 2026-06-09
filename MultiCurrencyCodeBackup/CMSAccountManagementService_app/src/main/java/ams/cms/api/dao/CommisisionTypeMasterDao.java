package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.CommisisionTypeMaster;
import ams.cms.dao.GenericDao;

public interface CommisisionTypeMasterDao extends GenericDao<CommisisionTypeMaster> 
{
	List<CommisisionTypeMaster> getGLAccountInfoBasedOnCommisionType(CommisisionTypeMaster commisisionTypeMaster);
}
