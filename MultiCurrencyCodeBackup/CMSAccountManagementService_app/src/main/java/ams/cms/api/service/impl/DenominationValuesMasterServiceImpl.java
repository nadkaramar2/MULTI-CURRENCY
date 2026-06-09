package ams.cms.api.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.DenominationValuesMasterDao;
import ams.cms.api.model.DenominationValuesMaster;
import ams.cms.api.service.DenominationValuesMasterService;

@Transactional
@Service
public class DenominationValuesMasterServiceImpl implements DenominationValuesMasterService
{
	@Autowired
	DenominationValuesMasterDao denominationValuesMasterDao;
	
	@Override
	public DenominationValuesMaster getDenominationValuesMasterBasedOnParameters(	DenominationValuesMaster denominationValuesMaster) throws Exception 
	{
		return denominationValuesMasterDao.getDenominationValuesMasterBasedOnParameters(denominationValuesMaster);
	}
	
}
