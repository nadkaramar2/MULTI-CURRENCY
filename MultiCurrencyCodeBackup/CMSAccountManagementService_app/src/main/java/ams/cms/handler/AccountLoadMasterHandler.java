package ams.cms.handler;

import ams.cms.model.TxnReqRes;

public interface AccountLoadMasterHandler 
{
	TxnReqRes processTransactionToLoadBalance(TxnReqRes txnReqRes);
}
