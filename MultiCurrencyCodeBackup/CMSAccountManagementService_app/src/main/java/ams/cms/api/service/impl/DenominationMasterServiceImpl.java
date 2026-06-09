package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.DenominationMasterDao;
import ams.cms.api.model.DenominationMaster;
import ams.cms.api.service.DenominationMasterService;

@Transactional
@Service
public class DenominationMasterServiceImpl implements DenominationMasterService
{
	@Autowired
	DenominationMasterDao denominationMasterDao;

	@Override
	public DenominationMaster saveDenominationMasterData(DenominationMaster denominationMaster) throws Exception {
		denominationMasterDao.save(denominationMaster);
		return denominationMaster;
	}
	
	@Override
	public List<DenominationMaster> getTxnDenominationDetails(DenominationMaster denominationMaster) {

		return denominationMasterDao.getTxnDenominationDetails(denominationMaster);
	}

	public List<DenominationMaster> getTxnDenominationDetailsbyAgent(DenominationMaster denominationMaster) {

		return denominationMasterDao.getTxnDenominationDetailsbyAgent(denominationMaster);
	}
}
