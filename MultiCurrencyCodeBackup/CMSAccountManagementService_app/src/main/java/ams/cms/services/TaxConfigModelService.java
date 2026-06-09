package ams.cms.services;

import java.util.List;

import ams.cms.model.TaxConfigModel;

public interface TaxConfigModelService
{
	List<TaxConfigModel> getTaxTypeConfigModelsList(TaxConfigModel taxConfigModel) throws Exception;
}
