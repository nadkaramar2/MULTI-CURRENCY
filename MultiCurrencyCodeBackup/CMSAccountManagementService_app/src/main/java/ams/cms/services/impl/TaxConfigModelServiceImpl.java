package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.TaxConfigModelDao;
import ams.cms.model.TaxConfigModel;
import ams.cms.services.TaxConfigModelService;

@Transactional
@Service
public class TaxConfigModelServiceImpl implements TaxConfigModelService
{
	@Autowired
	TaxConfigModelDao taxConfigModelDao;

	@Override
	public List<TaxConfigModel> getTaxTypeConfigModelsList(TaxConfigModel taxConfigModel) throws Exception 
	{
		return taxConfigModelDao.getTaxTypeConfigModelsList(taxConfigModel);
	}

}
