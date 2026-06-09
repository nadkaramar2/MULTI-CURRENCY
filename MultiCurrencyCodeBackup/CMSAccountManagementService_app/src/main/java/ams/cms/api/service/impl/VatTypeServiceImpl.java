package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.VatTypeMasterDao;
import ams.cms.api.model.VatTypeMaster;
import ams.cms.api.service.VatTypeService;

@Transactional
@Service
public class VatTypeServiceImpl implements VatTypeService{

	@Autowired
	VatTypeMasterDao vatTypeMasterDao;
	
	@Override
	public List<VatTypeMaster> getVatCollectedBalance(VatTypeMaster vatTypeMaster) {
		
		return vatTypeMasterDao.getVatCollectedBalance(vatTypeMaster);
	}

}
