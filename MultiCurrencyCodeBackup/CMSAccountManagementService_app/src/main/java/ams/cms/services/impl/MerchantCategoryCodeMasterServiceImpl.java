package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.MerchantCategoryCodeMasterDao;
import ams.cms.model.MerchantCategoryCodeMaster;
import ams.cms.services.MerchantCategoryCodeMasterService;

@Transactional
@Service
public class MerchantCategoryCodeMasterServiceImpl implements MerchantCategoryCodeMasterService
{
	@Autowired
	MerchantCategoryCodeMasterDao merchantCategoryCodeMasterDao;
	
	@Override
	public List<MerchantCategoryCodeMaster> getAllListOfMerchantCategoryCode(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception
	{
		List<MerchantCategoryCodeMaster> listdata = merchantCategoryCodeMasterDao.findAll();
		return listdata;
	}
	
	@Override
	public List<MerchantCategoryCodeMaster> getSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception 
	{
		return merchantCategoryCodeMasterDao.getSelectedMccAndDescrList(merchantCategoryCodeMaster);
	}
	
	@Override
	public List<MerchantCategoryCodeMaster> getUnSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception 
	{
		return merchantCategoryCodeMasterDao.getUnSelectedMccAndDescrList(merchantCategoryCodeMaster);
	}
}
