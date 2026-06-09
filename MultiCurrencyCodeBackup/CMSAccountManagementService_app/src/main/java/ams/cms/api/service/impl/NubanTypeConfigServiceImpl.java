package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.NubanTypeConfigDao;
import ams.cms.api.model.NubanTypeConfig;
import ams.cms.api.service.NubanTypeConfigService;

@Transactional
@Service
public class NubanTypeConfigServiceImpl implements NubanTypeConfigService{

	@Autowired
	NubanTypeConfigDao nubanTypeConfigDao;
	
	@Override
	public List<NubanTypeConfig> getNubanTypes(){
		return nubanTypeConfigDao.findAll();
	}

	@Override
	public NubanTypeConfig getNubanTypeDescription(NubanTypeConfig nubanTypeConfig) {
		return nubanTypeConfigDao.findDescriptionByType(nubanTypeConfig);
	}
	
}
