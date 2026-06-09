package ams.cms.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ParticipantMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.ParticipantAppInfoMaster;
import ams.cms.model.ParticipantMaster;
import ams.cms.services.ParticipantAppInfoMasterService;
import ams.cms.services.ParticipantMasterService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class ParticipantMasterServiceImpl implements ParticipantMasterService 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ParticipantMasterServiceImpl.class);
	
	private static final String PARTICIPANT_SEPARATOR = "|$|";
	
	@Autowired
	private	ParticipantMasterDao participantDao;
	
	@Autowired
	private ParticipantAppInfoMasterService participantAppInfoMasterService;

	@Override
	public ParticipantMaster addParticipant(ParticipantMaster participantMaster) throws Exception
	{
		try 
		{
			boolean isExist = isParticipantAppInfoExist(participantMaster);			
			if (!isExist) 
			{
				addParticipantAppInfo(participantMaster);
				participantMaster.setStrCreatedDate(Utils.getCurrentDate());
				participantDao.save(participantMaster);
			}
			else 
			{
				updateParticipantAppInfo(participantMaster);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
		return participantMaster;
	}

	@Override
	public List<ParticipantMaster> getAllParticipantList(ParticipantMaster participantMaster) 
	{
		List<ParticipantMaster> participantMasterList = participantDao.getAllParticipantList(participantMaster);
		return participantMasterList;
	}
	
	@Override
	public String generateAPIkEYParticipantWise(ParticipantMaster participantMaster)
	{
		String result = "";
		try 
		{
			String participantId = participantMaster.getStrParticipantID();
			String ParticipantName = participantMaster.getStrParticipantName();
			
			Date date = new Date();    
			DateFormat day = new SimpleDateFormat("dd");
			DateFormat month = new SimpleDateFormat("MMM");
			DateFormat year = new SimpleDateFormat("YYYY");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());     
			
			String apiKeyFormat =  participantId + PARTICIPANT_SEPARATOR + ParticipantName + PARTICIPANT_SEPARATOR + day.format(date) + PARTICIPANT_SEPARATOR +  month.format(date) + PARTICIPANT_SEPARATOR + year.format(date) + PARTICIPANT_SEPARATOR + timestamp.getTime();
			amsLogger.writeInfoLog("apiKeyFormat::"+apiKeyFormat);			
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(apiKeyFormat);
			oos.flush();
			
			result = new String(Base64.encodeBase64(bos.toByteArray()));
			amsLogger.writeInfoLog("result::"+result);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return result;
	}

	@Override
	public String getApiKeyBasedOnParticipant(ParticipantMaster participantMaster) 
	{
		return participantDao.getApiKeyBasedOnParticipant(participantMaster);
	}
	
	private void addParticipantAppInfo(ParticipantMaster participantMaster) 
	{
		try 
		{
			ParticipantAppInfoMaster participantAppInfoMaster = new ParticipantAppInfoMaster();
			participantAppInfoMaster.setStrParticipantId(participantMaster.getStrParticipantID());
			participantAppInfoMaster.setStrParticipantName(participantMaster.getStrParticipantName());
			
			participantAppInfoMaster = participantAppInfoMasterService.addParticipantAppInfo(participantAppInfoMaster);
			
			if (participantAppInfoMaster!=null && participantAppInfoMaster.getStrApiKey()!=null) 
			{
				participantMaster.setStrApikey(participantAppInfoMaster.getStrApiKey());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private int updateParticipantAppInfo(ParticipantMaster participantMaster) 
	{
		try 
		{
			ParticipantAppInfoMaster participantAppInfoMaster = new ParticipantAppInfoMaster();
			participantAppInfoMaster.setStrParticipantId(participantMaster.getStrParticipantID());
			participantAppInfoMaster.setStrParticipantName(participantMaster.getStrParticipantName());
			
			return participantAppInfoMasterService.updateParticipantAppInfo(participantAppInfoMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	private boolean isParticipantAppInfoExist(ParticipantMaster participantMaster) 
	{
		boolean isExist = false;
		try 
		{
			String participantId = participantMaster.getStrParticipantID();
			ParticipantAppInfoMaster participantAppInfoMaster = new ParticipantAppInfoMaster();
			participantAppInfoMaster.setStrParticipantId(participantId);
			
			ParticipantAppInfoMaster participantAppInfoMasterInstance = participantAppInfoMasterService.getParticipantAppInfoObject(participantAppInfoMaster);
			
			if (participantAppInfoMasterInstance != null && participantAppInfoMasterInstance.getStrID() != null) 
			{
				participantMaster.setStrApikey(participantAppInfoMasterInstance.getStrApiKey());
				isExist = true;
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return isExist;
	}
}
