package ams.cms.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ams.cms.dao.TransactionIdDao;
import ams.cms.dao.impl.TransactionIdDaoImpl;
import ams.cms.logger.AMSLogger;
import ams.cms.model.TransactionIdTable;
import ams.cms.services.TransactionIdService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class TransactionIdServiceImpl implements TransactionIdService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionIdServiceImpl.class);
	
	@Autowired
	private TransactionIdDao transactionIdDao;
	
	@Override
	public List<TransactionIdTable> getTransactionIdList(String year, String julianDayFormat) {
		return transactionIdDao.getTransactionIdList( year, julianDayFormat);
	}

	//@Transactional
	@Override
	public void saveTransactionIdDetails(TransactionIdTable transactionIdTable) {
		transactionIdDao.save(transactionIdTable);
	}

	@Override
	public int updateTransactionIdDetails(TransactionIdTable transactionIdTable) {
		return transactionIdDao.updateTransactionIdDetails(transactionIdTable);		
	}

	@Override
	public void savePreTransactionIdDetails(TransactionIdTable transactionIdTable) 
	{
		transactionIdDao.savePreTransactionIdDetails(transactionIdTable);		
	}

	@Override
	public int updatePreTransactionIdDetails(TransactionIdTable transactionIdTable) 
	{
		return transactionIdDao.updatePreTransactionIdDetails(transactionIdTable);
	}

	@Override
	public List<TransactionIdTable> getPreTransactionIdList(String year, String julianDate) {
		return transactionIdDao.getPreTransactionIdList(year, julianDate);
	}

	@Override
	public String getTransactionId() 
	{
		return getNewTransactionId();
		
		/*
		String transactionId = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) 
			{
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) 
			{
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			String newTxnSerialNumber = year + julianDayFormat + "0000001";
			
			/*
						
			String Date = dateFormat.format(new Date());
			
			TransactionIdTable transactionIdTable = null;			
			List<TransactionIdTable> transactionIdTables = getTransactionIdList(year, julianDayFormat);
			if(transactionIdTables.isEmpty()) 
			{
				String yearJulianDateFormatData = year + julianDayFormat;
				
				transactionIdTable = new TransactionIdTable();
				transactionId = yearJulianDateFormatData + "0000001";
				transactionIdTable.setStrYear(year);
				transactionIdTable.setStrJulianDate(julianDayFormat);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				transactionIdTable.setStrCreatedDate(Date);
				transactionIdTable.setStrCreatedBy("System");
				
				saveTransactionIdDetails(transactionIdTable);
			}
			else 
			{
				transactionIdTable = transactionIdTables.get(0); 
				transactionId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				transactionIdTable.setStrCreatedDate(Date);
				
				updateTransactionIdDetails(transactionIdTable);
			}
			
			transactionId = transactionIdDao.getNextTransactionId(year, julianDayFormat, newTxnSerialNumber);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionId;
		*/	
	}

	@Override
	public String getPreTransactionId() 
	{
		String transactionId = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) 
			{
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) 
			{
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			String Date = dateFormat.format(new Date());
			
			TransactionIdTable transactionIdTable = null;			
			List<TransactionIdTable> transactionIdTables = getPreTransactionIdList(year, julianDayFormat);
			if(transactionIdTables.isEmpty()) 
			{
				String yearJulianDateFormatData = year + julianDayFormat;
				
				transactionIdTable = new TransactionIdTable();
				transactionId = yearJulianDateFormatData + "0000000001";
				transactionIdTable.setStrYear(year);
				transactionIdTable.setStrJulianDate(julianDayFormat);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				
				savePreTransactionIdDetails(transactionIdTable);
			}
			else 
			{
				transactionIdTable = transactionIdTables.get(0); 
				transactionId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				transactionIdTable.setStrCreatedDate(Date);
				
				updatePreTransactionIdDetails(transactionIdTable);
			}	   
			return transactionId;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionId;
	}

	@Async("multiThreadBean")
	@Override
	public CompletableFuture<String> getNextTransactionId() throws InterruptedException 
	{
		String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
		String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
		
		StringBuilder julianDayFormatSb = new StringBuilder("");
		
		if (dayOfCurrentYearinStr.length() == 1) 
		{
			julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
		}
		else if (dayOfCurrentYearinStr.length() == 2) 
		{
			julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
		}
		else
		{
			julianDayFormatSb.append(dayOfCurrentYearinStr);
		}
		String julianDayFormat = julianDayFormatSb.toString();
		
		String newTxnSerialNumber = year + julianDayFormat + "0000001";
		
		String transactionId = transactionIdDao.getNextTransactionId(year, julianDayFormat, newTxnSerialNumber);
		
		return CompletableFuture.completedFuture(transactionId);
	}

	@Override
	public String getNewTransactionId() 
	{
		String newTxnId = null;
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) 
			{
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) 
			{
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSSSSS");
			
			String currentStrDate = sdf.format(new Date()); 
			
			Date currentDate = sdf.parse(currentStrDate);
			long currentNanoSec = currentDate.getTime();			
			
			newTxnId = year + julianDayFormat + currentNanoSec + Utils.nDigitRandomNo(7);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}				
		return newTxnId;
	}

}
