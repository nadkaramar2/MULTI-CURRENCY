package ams.cms.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.PreAccountMasterTempDao;
import ams.cms.api.model.PreAccountMasterTemp;
import ams.cms.api.service.PreAccountMasterTempService;

@Transactional
@Service
public class PreAccountMasterTempServiceImpl implements PreAccountMasterTempService 
{
	@Autowired
	PreAccountMasterTempDao preAccountMasterTempDao;

	@Override
	public PreAccountMasterTemp saveSignUpDataTemporary(PreAccountMasterTemp preAccountMasterTemp) throws Exception 
	{
		preAccountMasterTempDao.save(preAccountMasterTemp);
		return preAccountMasterTemp;
	}

}
