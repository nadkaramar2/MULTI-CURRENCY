package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.PreSubAccountMasterDao;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.api.service.PreSubAccountMasterService;

@Transactional
@Service
public class PreSubAccountMasterServiceImpl implements PreSubAccountMasterService
{
	
	@Autowired
	PreSubAccountMasterDao preSubAccountMasterDao; 

	@Override
	public PreSubAccountMaster savePreSubAccountMaster(PreSubAccountMaster preSubAccMaster) throws Exception
	{
	     preSubAccountMasterDao.save(preSubAccMaster);
		return preSubAccMaster;
	}

	@Override
	public String getAccountTypeExist(PreSubAccountMaster preSubAccMaster) throws Exception {
		return preSubAccountMasterDao.getAccountTypeExist(preSubAccMaster);
	}

	@Override
	public int updateIsAccountNoCreatedField(PreSubAccountMaster preSubAccountMaster) throws Exception {
		return preSubAccountMasterDao.updateIsAccountNoCreatedField(preSubAccountMaster);
	}

	@Override
	public List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster) 
	{
		return preSubAccountMasterDao.getPendingRegCustWithLinkAccount(preSubAccountMaster);
	}

}
