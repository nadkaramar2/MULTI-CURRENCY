package ams.cms.api.utitlity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.model.ComplaintIdTable;
import ams.cms.api.service.ComplaintIdTableService;
//created By ankit 
/*
 * Note - Two object initialization was throwing error so will have to create a Seprate Utils
 * 	      for Complaint and Transaction
 * */
@Component
public class Utils 
{
	public static String txnId;
	
	public static String deviceId;
	
	private static ComplaintIdTableService complaintIdTableService;
	@Autowired
	public Utils(ComplaintIdTableService complaintIdTableServiceObj) {
		Utils.complaintIdTableService = complaintIdTableServiceObj;
	}
	
	public static String DateAndHoursFormat() 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public static Date getCurrentDateFormat() {
		Date date = new Date();
		return date;
	}
	
	public static String generateComplaintId(int num) {
		String complaintFormat = String.format("%05d", num);
		String complaintId = ("C" + complaintFormat);
		return complaintId;
	}
	
	public static Timestamp  getCurrentTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}
	
	public static Time getFormattedCurrentTime() throws Exception 
	{
		SimpleDateFormat timeFormat = new SimpleDateFormat();
		Date curretDate = new Date();
		String timeFormatdate = timeFormat.format(curretDate);
		return new java.sql.Time(timeFormat.parse(timeFormatdate).getTime());
	}
	
	public static String getGeneratedComplaintId() 
	{
		String complaintId = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) {
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) {
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else {
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			String Date = dateFormat.format(new Date());
			
			List<ComplaintIdTable> complaintIdTables = complaintIdTableService.getComplaintIdList(year, julianDayFormat);
			ComplaintIdTable complaintIdTable = null;
			
			if(complaintIdTables.isEmpty()) 
			{
				String yearJulianDateFormatData = year + julianDayFormat;
				complaintIdTable = new ComplaintIdTable();
				complaintId = yearJulianDateFormatData + "0000001";
				complaintIdTable.setStrYear(year);
				complaintIdTable.setStrJulianDate(julianDayFormat);
				complaintIdTable.setStrLastComplaintSerialNo(complaintId);
				complaintIdTable.setStrCreatedDate(Date);
				complaintIdTable.setStrCreatedBy("System"); 
				complaintIdTableService.saveComplaintIdDetails(complaintIdTable);
			}
			else 
			{
				complaintIdTable = complaintIdTables.get(0); 
				complaintId = String.valueOf(Long.parseLong(complaintIdTable.getStrLastComplaintSerialNo()) + 1);
				complaintIdTable.setStrLastComplaintSerialNo(complaintId);
				complaintIdTable.setStrCreatedDate(Date);
				complaintIdTableService.updateComplaintIdDetails(complaintIdTable);
			}	   
			return complaintId;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return complaintId;
	}
	public static String getTranCategoryType(String tranType)
	{
		String tranCategory = "";
		if ("P2P".equals(tranType)) 
		{
			tranCategory = "P";
		}
		else if ("P2M".equals(tranType)) {
			tranCategory = "M";
		}
		else if ("M2M".equals(tranType)) 
		{
			tranCategory = "M";
		}
		else 
		{
			tranCategory = "NA";
		}
		return tranCategory;
	}
	
	public static String getExpiryDateStr(String dbExpiryDateStr) 
	{
			try 
			{
				if (dbExpiryDateStr!=null && dbExpiryDateStr.trim().length() > 0)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("MM/YY");				
					SimpleDateFormat parsingSdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");				
					
					Date parsedDate = parsingSdf.parse(dbExpiryDateStr);				
					System.out.println("parsedDate::"+parsedDate);
					
					String expiryDateStr = sdf.format(parsedDate);				
					System.out.println("expiryDateStr::"+expiryDateStr);
					
					return expiryDateStr;
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return "";
	}
	
	//added by Sunil Y
	public static String getCurrentTimeInString() 
	{
		try 
		{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");			
			String currentTime = df.format(new Date());
			return currentTime;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getCurrentDate() 
	{
		return new Date();
	}
	
	public static Connection getConnectDB(String databaseUrl, String userName, String password)	 
    {
        try 
        {
            // Importing and registering drivers
            Class.forName("com.mysql.jdbc.Driver");
 
            Connection con = DriverManager.getConnection(databaseUrl, userName, password);
            return con;
        }
        catch (SQLException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
	
	public static int nDigitRandomNo(int digits)
	{
	    int max = (int) Math.pow(10,(digits)) - 1; //for digits =7, max will be 9999999
	    int min = (int) Math.pow(10, digits-1); //for digits = 7, min will be 1000000
	    int range = max-min; //This is 8999999
	    Random r = new Random(); 
	    int x = r.nextInt(range);// This will generate random integers in range 0 - 8999999
	    int nDigitRandomNo = x+min; //Our random rumber will be any random number x + min
	    return nDigitRandomNo;
	}

	public static double stringToDouble(String strTxnAmount) {
		double convertValue = Double.parseDouble(strTxnAmount);
		return convertValue;
	}

	public static int stringToInt(String value) {
		int convertValue = Integer.parseInt(value);
		return convertValue;
	}
}
