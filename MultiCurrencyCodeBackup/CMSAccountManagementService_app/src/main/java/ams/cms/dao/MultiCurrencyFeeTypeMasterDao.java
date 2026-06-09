package ams.cms.dao;

import ams.cms.model.MultiCurrencyFeeTypeMaster;

public interface MultiCurrencyFeeTypeMasterDao extends GenericDao<MultiCurrencyFeeTypeMaster> {

	MultiCurrencyFeeTypeMaster getFeeTypeDetails(MultiCurrencyFeeTypeMaster feeTypeMaster);

}
