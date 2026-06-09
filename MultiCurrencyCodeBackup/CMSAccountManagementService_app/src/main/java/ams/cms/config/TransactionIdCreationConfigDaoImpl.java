package ams.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.services.TransactionIdService;

@Component
public class TransactionIdCreationConfigDaoImpl implements TransactionIdCreationConfigDao 
{
	@Autowired
	private TransactionIdService transactionIdService;
	
	@Override
	public String getTransactionId() throws Exception 
	{
		return transactionIdService.getTransactionId();
	}
	@Override
	public String getPreTransactionId() throws Exception 
	{
		return transactionIdService.getPreTransactionId();
	}
	
	/*
	@Override
	public String getTransactionId() throws Exception 
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
			List<TransactionIdTable> transactionIdTables = transactionIdService.getTransactionIdList(year, julianDayFormat);
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
				
				transactionIdService.saveTransactionIdDetails(transactionIdTable);
			}
			else 
			{
				transactionIdTable = transactionIdTables.get(0); 
				transactionId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				transactionIdTable.setStrCreatedDate(Date);
				
				transactionIdService.updateTransactionIdDetails(transactionIdTable);
			}	   
			return transactionId;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return transactionId;
	}
	

	@Override
	public String getPreTransactionId() throws Exception 
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
			List<TransactionIdTable> transactionIdTables = transactionIdService.getPreTransactionIdList(year, julianDayFormat);
			if(transactionIdTables.isEmpty()) 
			{
				String yearJulianDateFormatData = year + julianDayFormat;
				
				transactionIdTable = new TransactionIdTable();
				transactionId = yearJulianDateFormatData + "0000000001";
				transactionIdTable.setStrYear(year);
				transactionIdTable.setStrJulianDate(julianDayFormat);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				
				transactionIdService.savePreTransactionIdDetails(transactionIdTable);
			}
			else 
			{
				transactionIdTable = transactionIdTables.get(0); 
				transactionId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				transactionIdTable.setStrLastTxnSerialNo(transactionId);
				transactionIdTable.setStrCreatedDate(Date);
				
				transactionIdService.updatePreTransactionIdDetails(transactionIdTable);
			}	   
			return transactionId;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return transactionId;
	
	}
	*/
}
