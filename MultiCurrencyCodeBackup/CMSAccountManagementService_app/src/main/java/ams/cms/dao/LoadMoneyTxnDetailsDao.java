package ams.cms.dao;

import ams.cms.model.LoadMoneyTxnDetails;

public interface LoadMoneyTxnDetailsDao extends GenericDao<LoadMoneyTxnDetails>  {

	LoadMoneyTxnDetails getPreviousLoadMoneyData(LoadMoneyTxnDetails loadMoneyTxnDetails);

	LoadMoneyTxnDetails getLoadDetailTxnByAccountNumber(LoadMoneyTxnDetails loadMoneyTxnDetails);

}
