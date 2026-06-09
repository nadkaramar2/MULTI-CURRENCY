package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ChargeRelatedMasterDao;
import ams.cms.model.ChargeRelatedMaster;
import ams.cms.services.ChargeRelatedMasterService;

@Transactional
@Service
public class ChargeRelatedMasterServiceImpl implements ChargeRelatedMasterService{
	
	@Autowired
	ChargeRelatedMasterDao chargeRelatedMasterDao;

	@Override
	public List<ChargeRelatedMaster> getChargeRelatedList(ChargeRelatedMaster chargeRelatedMaster) throws Exception {

		return chargeRelatedMasterDao.getChargeRelatedList(chargeRelatedMaster);
	}

	@Override
	public String getChargeRelatedDescription(ChargeRelatedMaster chargeRelatedMaster) {
		return chargeRelatedMasterDao.getChargeRelatedDescription(chargeRelatedMaster);
	}

	@Override
	public ChargeRelatedMaster addChargeRelated(ChargeRelatedMaster chargeRelatedMaster) {
		chargeRelatedMasterDao.save(chargeRelatedMaster);
		return chargeRelatedMaster;
	}

	@Override
	public Boolean validateChargeRelated(ChargeRelatedMaster chargeRelatedMaster) {
		return chargeRelatedMasterDao.validateChargeRelated(chargeRelatedMaster);
	}

	
	
}
