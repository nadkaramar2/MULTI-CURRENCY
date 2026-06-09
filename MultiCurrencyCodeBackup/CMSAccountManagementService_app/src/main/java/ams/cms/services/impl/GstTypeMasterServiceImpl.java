package ams.cms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.GstTypeMasterDao;
import ams.cms.model.GstTypeMaster;
import ams.cms.services.GstTypeMasterService;

@Service
public class GstTypeMasterServiceImpl implements GstTypeMasterService {

	@Autowired
	private GstTypeMasterDao gstTypeMasterDao;
	
	@Override
	public GstTypeMaster getGstTypeMasterByGstType(GstTypeMaster gstTypeMaster) {
		
		return gstTypeMasterDao.getGstTypeMasterByGstType(gstTypeMaster);
	}
	

}
