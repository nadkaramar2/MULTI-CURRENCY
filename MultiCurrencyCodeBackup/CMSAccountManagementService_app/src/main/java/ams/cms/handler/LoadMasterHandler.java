package ams.cms.handler;

import ams.cms.model.TxnReqRes;
import ams.cms.util.FundTransferProcessResponse;

public interface LoadMasterHandler 
{
	//TxnReqRes processTransactionToLoadBalance(TxnReqRes txnReqRes) throws Exception ;
	FundTransferProcessResponse processTransactionToLoadBalance(TxnReqRes txnReqRes) throws Exception ;

}
