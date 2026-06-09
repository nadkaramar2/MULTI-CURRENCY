package ams.cms.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ParticipantAppInfoMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantAppInfoMaster;
import ams.cms.services.ParticipantAppInfoMasterService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class ParticipantAppInfoMasterServiceImpl implements ParticipantAppInfoMasterService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ParticipantAppInfoMasterServiceImpl.class);
	
	private static final String PARTICIPANT_SEPARATOR = "_$";
	private static final String SECRET_KEY_SEPARATOR = "$#@";
	
	@Autowired
	private ParticipantAppInfoMasterDao participantAppInfoMasterDao;
	
	@Override
	public ParticipantAppInfoMaster addParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception 
	{
		participantAppInfoMaster = getParticipantAppInfoMasterWithApikey(participantAppInfoMaster);		
		participantAppInfoMasterDao.save(participantAppInfoMaster);		
		return participantAppInfoMaster;
	}
	
	private ParticipantAppInfoMaster getParticipantAppInfoMasterWithApikey(ParticipantAppInfoMaster participantAppInfoMaster) 
	{
		try 
		{
			String apiKey = getApiKeyParticipantWise(participantAppInfoMaster);
			participantAppInfoMaster.setStrApiKey(apiKey);
			
			apiKey = apiKey.substring(0, apiKey.indexOf("SEPARATOR"));
			
			String secretKey = Utils.getSecretKey(apiKey, SECRET_KEY_SEPARATOR);
			
			String phrase = Utils.getPhrase();		
			String iv = Utils.getIv();
			String salt = Utils.getSalt();
			
			participantAppInfoMaster.setStrSecretKey(secretKey);
			
			participantAppInfoMaster.setStrPhrase(phrase);
			participantAppInfoMaster.setStrIv(iv);
			participantAppInfoMaster.setStrSalt(salt);
			
			participantAppInfoMaster.setStrCreatedDate(Utils.getCurrentDate());
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return participantAppInfoMaster;
	}
	
	private String getApiKeyParticipantWise(ParticipantAppInfoMaster participantMaster) 
	{
		String result = "";
		try 
		{
			String participantId = participantMaster.getStrParticipantId();
			String ParticipantName = participantMaster.getStrParticipantName();
			
			Date date = new Date();    
			DateFormat day = new SimpleDateFormat("dd");
			DateFormat month = new SimpleDateFormat("MMM");
			DateFormat year = new SimpleDateFormat("YYYY");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
			
			participantId = participantId.trim();
			
			String apiKeyFormat =  participantId + PARTICIPANT_SEPARATOR + ParticipantName + PARTICIPANT_SEPARATOR + day.format(date) + PARTICIPANT_SEPARATOR +  month.format(date) + PARTICIPANT_SEPARATOR + year.format(date) + PARTICIPANT_SEPARATOR + timestamp.getTime();
			amsLogger.writeInfoLog("apiKeyFormat::"+apiKeyFormat);			
			
			//String hashResult = Utils.generateHash(apiKeyFormat);
			//amsLogger.writeInfoLog("hashResult::"+hashResult);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(apiKeyFormat);
			oos.flush();
			
			result = new String(Base64.encodeBase64(bos.toByteArray()));
			amsLogger.writeInfoLog("result::"+result);
			
			result = result+"SEPARATOR"+participantId;
			amsLogger.writeInfoLog("Final result::"+result);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	//@Transactional
	@Override
	public ParticipantAppInfoMaster getParticipantAppInfoObject(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception 
	{
		return participantAppInfoMasterDao.getParticipantAppInfoObject(participantAppInfoMaster);
	}

	@Override
	public int updateParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception 
	{
		participantAppInfoMaster = getParticipantAppInfoMasterWithApikey(participantAppInfoMaster);
		return participantAppInfoMasterDao.updateParticipantAppInfo(participantAppInfoMaster);
	}
}
