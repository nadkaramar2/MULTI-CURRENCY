package ams.cms.dao;

import java.util.List;

import ams.cms.model.TaxConfigModel;

public interface TaxConfigModelDao extends GenericDao<TaxConfigModel>
{
	List<TaxConfigModel> getTaxTypeConfigModelsList(TaxConfigModel taxConfigModel);
}
