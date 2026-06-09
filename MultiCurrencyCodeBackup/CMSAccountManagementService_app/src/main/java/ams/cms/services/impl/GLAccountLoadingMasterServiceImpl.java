package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.GLAccountLoadingMasterDao;
import ams.cms.model.GLAccountLoadingMaster;
import ams.cms.services.GLAccountLoadingMasterService;

@Transactional
@Service
public class GLAccountLoadingMasterServiceImpl implements GLAccountLoadingMasterService
{
	@Autowired
	GLAccountLoadingMasterDao glAccountLoadingMasterDao;

	@Override
	public GLAccountLoadingMaster saveGLAccountLoadMasterInfo(GLAccountLoadingMaster glAccountLoadingMaster) throws Exception 
	{
		glAccountLoadingMaster.setDateOfLoading(new Date());
		glAccountLoadingMasterDao.save(glAccountLoadingMaster);
		return glAccountLoadingMaster;
	}
	
	
}
