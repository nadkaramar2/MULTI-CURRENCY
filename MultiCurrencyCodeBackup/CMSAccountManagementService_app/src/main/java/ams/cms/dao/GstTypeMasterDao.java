package ams.cms.dao;

import ams.cms.model.GstTypeMaster;

public interface GstTypeMasterDao extends GenericDao<GstTypeMaster>{

	GstTypeMaster getGstTypeMasterByGstType(GstTypeMaster gstTypeMaster);

}
