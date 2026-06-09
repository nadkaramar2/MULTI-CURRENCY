package ams.cms.config;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.utility.Utils;


@Component
public class CustomerIdCreationConfig implements CustomerIdCreationConfigIF
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CustomerIdCreationConfig.class);
	
	@Autowired
	private CustomerIDCreationService customerIDCreationService;
	
	@Override
	public CustomerIdMap getCustId() 
	{
		String customerID = null;		
		Map<String, String> CustomerIdMapValues = new HashMap<String, String>();	
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
			
			CustomerIdMapValues.put("julianYear", year);
			CustomerIdMapValues.put("julianDate", julianDayFormat);
			
			CustomerIdMap CustomerIdMap = new CustomerIdMap();
			CustomerIdMap.setStrYear(year);
			CustomerIdMap.setStrJulianDate(julianDayFormat);
			
			String custIdvalue = customerIDCreationService.getCustId(year, julianDayFormat);			
			if(custIdvalue != null && custIdvalue.trim().length() > 0)
			{
				String getIncrementalSerialNo = custIdvalue.substring(0, custIdvalue.length() - julianDayFormat.length());
				
				int incrementalSerialNo = Integer.parseInt(getIncrementalSerialNo);				
				incrementalSerialNo = incrementalSerialNo + 1;				
				customerID = String.valueOf(incrementalSerialNo).concat(julianDayFormat);
				
				CustomerIdMapValues.put("action", "U");
				CustomerIdMapValues.put("cust_id", customerID);
				CustomerIdMap.setStrAction("U");
			}
			else 
			{ 
				customerID = year + "000001" + julianDayFormat;
				CustomerIdMapValues.put("action", "A");
				CustomerIdMapValues.put("cust_id", customerID);
				CustomerIdMap.setStrAction("A");
			}	   
			 
			CustomerIdMap.setStrCustID(customerID);
			CustomerIdMapValues.put("cust_id", customerID );
			return CustomerIdMap;
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public int updateCustId(CustomerIdCreation customerIdCreation)
	{
		try 
		{
			Date birthDate = Utils.simpleDateFormat.parse(customerIdCreation.getStrDOB());
			customerIdCreation.setBirthDate(birthDate);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
		return customerIDCreationService.insertCustId(customerIdCreation);
	}

	@Override
	public List<CustomerIdCreation> getcustomerdetailsbyId(CustomerIdCreation customerIdCreation) 
	{
		return customerIDCreationService.getcustomerdetailsbyId(customerIdCreation);
	}
}
