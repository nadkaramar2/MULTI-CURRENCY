package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeCharges;
import ams.cms.model.ChargeMaster;

public interface ChargeMasterDao extends GenericDao<ChargeMaster> {
	
	int[] batchEntryforChargingConfigData(List<AccountTypeCharges> accountTypeCharges);
	
	List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges);

	List<ChargeMaster> getChargeMasterList(ChargeMaster chargeMaster);

	List<ChargeMaster> getTransactionChargeList(ChargeMaster chargeMaster) throws Exception;

	List<ChargeMaster> getFuelChargeList(ChargeMaster chargeMaster) throws Exception;
	
	String getChargeDescriptionBasedOnChargeType(String strChargeType);
	
	Boolean validateChargeType(ChargeMaster chargeMaster) throws Exception;	
}
