package ams.cms.services;

import java.util.Date;

import ams.cms.config.AppInfo;
import ams.cms.utility.PayloadReqRes;

public interface TxnReqRespLogMasterService 
{
	void addTxnRequestResponseLogData(PayloadReqRes req, AppInfo appInfo, Object resObject, Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus);
	
	void addTxnReqRespLogData(Object req, AppInfo appInfo, Object resObject, Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus);
	
	public void addTxnReqRespLogData(Object req, AppInfo appInfo, Object plainTxnReq, Object resObject, Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus);
}
