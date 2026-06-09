package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.TcsTypeMasterDao;
import ams.cms.model.TcsTypeMaster;
import ams.cms.services.TcsTypeMasterService;

@Transactional
@Service
public class TcsTypeMasterServiceImpl implements TcsTypeMasterService {

	@Autowired
	private TcsTypeMasterDao tcsTypeMasterDao;
	
	@Override
	public TcsTypeMaster getTcsTypeMasterObj(TcsTypeMaster tcsTypeMaster) {
		return tcsTypeMasterDao.getTcsTypeMasterObj(tcsTypeMaster);
	}

}
