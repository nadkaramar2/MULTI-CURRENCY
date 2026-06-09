package ams.cms.services.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.AppInfo;
import ams.cms.dao.TxnReqRespLogMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TxnReqRespLogMaster;
import ams.cms.services.TxnReqRespLogMasterService;
import ams.cms.utility.PayloadReqRes;

@Transactional
@Service
public class TxnReqRespLogMasterServiceImpl implements TxnReqRespLogMasterService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TxnReqRespLogMasterServiceImpl.class);
	
	@Autowired
	private TxnReqRespLogMasterDao txnReqRespLogMasterDao;

	@Override
	public void addTxnRequestResponseLogData(PayloadReqRes req, AppInfo appInfo, Object resObject,  Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus) 
	{
		try 
		{
			String requestPayloadJson = getJsonObjectInStr(req);
			
			String txnResponseJson = getJsonObjectInStr(resObject);
			
			TxnReqRespLogMaster txnReqRespLogMaster = new TxnReqRespLogMaster();
			txnReqRespLogMaster.setApiKey(appInfo.getApiKey());
			txnReqRespLogMaster.setApiHash(appInfo.getApiHash()); 
			txnReqRespLogMaster.setSecretKey(appInfo.getSecretKey());
			txnReqRespLogMaster.setReqPayload(requestPayloadJson);
			
			txnReqRespLogMaster.setParticipantId(participantId);
			
			txnReqRespLogMaster.setTxnId(txnId);
			txnReqRespLogMaster.setMontraTxnId(montraTxnId);
			
			txnReqRespLogMaster.setTxnEndpoint(txnRequestEndpoint); 
			
			txnReqRespLogMaster.setReqTxnDate(txnRequestedDate);
			txnReqRespLogMaster.setResTxnDate(txnResponseDate);
			
			txnReqRespLogMaster.setTxnRequest(appInfo.getDecryptedPayload());
			txnReqRespLogMaster.setTxnResponse(txnResponseJson);
			
			txnReqRespLogMaster.setTxnStatus(txnStatus);
			
			txnReqRespLogMasterDao.save(txnReqRespLogMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	//@Transactional
	@Override
	public void addTxnReqRespLogData(Object req, AppInfo appInfo, Object resObject, Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus) 
	{
		try 
		{
			String requestPayloadJson = getJsonObjectInStr(req);
			
			String txnResponseJson = getJsonObjectInStr(resObject);
			
			TxnReqRespLogMaster txnReqRespLogMaster = new TxnReqRespLogMaster();
			txnReqRespLogMaster.setApiKey(appInfo.getApiKey());
			txnReqRespLogMaster.setApiHash(appInfo.getApiHash()); 
			txnReqRespLogMaster.setSecretKey(appInfo.getSecretKey());
			txnReqRespLogMaster.setReqPayload(requestPayloadJson);
			
			txnReqRespLogMaster.setParticipantId(participantId);
			
			txnReqRespLogMaster.setTxnId(txnId);
			txnReqRespLogMaster.setMontraTxnId(montraTxnId);
			
			txnReqRespLogMaster.setTxnEndpoint(txnRequestEndpoint); 
			
			txnReqRespLogMaster.setReqTxnDate(txnRequestedDate);
			txnReqRespLogMaster.setResTxnDate(txnResponseDate);
			
			txnReqRespLogMaster.setTxnRequest(appInfo.getDecryptedPayload());
			txnReqRespLogMaster.setTxnResponse(txnResponseJson);
			
			txnReqRespLogMaster.setTxnStatus(txnStatus);
			
			txnReqRespLogMasterDao.save(txnReqRespLogMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
	}
	
	private String getJsonObjectInStr(Object object) 
	{
		String jsonStr = null;
		try 
		{
			if (object == null) 
			{
				return jsonStr;
			}
			
			ObjectMapper mapper = new ObjectMapper(); 
			mapper.setSerializationInclusion(Include.NON_NULL);
			
			jsonStr = mapper.writeValueAsString(object);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return jsonStr;
	}

	@Override
	public void addTxnReqRespLogData(Object req, AppInfo appInfo, Object plainTxnReq, Object resObject, Date txnRequestedDate, Date txnResponseDate, String participantId, String txnId, String montraTxnId, String txnRequestEndpoint, String txnStatus) 
	{
		try 
		{
			String requestPayloadJson = getJsonObjectInStr(req);
			
			String txnResponseJson = getJsonObjectInStr(resObject);
			String txnRequestJson = getJsonObjectInStr(plainTxnReq);
			
			TxnReqRespLogMaster txnReqRespLogMaster = new TxnReqRespLogMaster();
			txnReqRespLogMaster.setApiKey(appInfo.getApiKey());
			txnReqRespLogMaster.setApiHash(appInfo.getApiHash()); 
			txnReqRespLogMaster.setSecretKey(appInfo.getSecretKey());
			txnReqRespLogMaster.setReqPayload(requestPayloadJson);
			
			txnReqRespLogMaster.setParticipantId(participantId);
			
			txnReqRespLogMaster.setTxnId(txnId);
			txnReqRespLogMaster.setMontraTxnId(montraTxnId);
			
			txnReqRespLogMaster.setTxnEndpoint(txnRequestEndpoint); 
			
			txnReqRespLogMaster.setReqTxnDate(txnRequestedDate);
			txnReqRespLogMaster.setResTxnDate(txnResponseDate);
			
			txnReqRespLogMaster.setTxnRequest(txnRequestJson);
			txnReqRespLogMaster.setTxnResponse(txnResponseJson);
			
			txnReqRespLogMaster.setTxnStatus(txnStatus);
			
			txnReqRespLogMasterDao.save(txnReqRespLogMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}

	
}
