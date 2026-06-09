package ams.cms.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ams.cms.api.controller.SignUpController;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.CustomerIdCreation;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.model.TransactionIdTable;
import ams.cms.services.TransactionIdService;
import ams.cms.util.AesUtil;


public class Utils 
{
	private static AMSLogger amsLogger = AMSLogger.getInstance(SignUpController.class);

	public static String txnId = "232700000001";
	
	private static CustomerIDCreationService customerMasterService;
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");	
	public static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
	
	public static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MMM/YYYY");
	public static SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("dd MMM YYYY");	
	public static SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("dd/MM/yyyy");
	
	public static SimpleDateFormat simpleDateTimeFormat2 = new SimpleDateFormat("dd MMM YYYY hh:mm a");	
	
	public static SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat simpleTimeFormat1 = new SimpleDateFormat("hh:mm a");
	
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	private static TransactionIdService transactionIdService;
	
	public static DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	@Autowired
	public Utils(CustomerIDCreationService customerMasterService) {
		Utils.customerMasterService = customerMasterService;
	}
	
	@Autowired
	public Utils(EmailService emailService) {
	}
	
	@Autowired
	public Utils(TransactionIdService transactionIdService) {
		Utils.transactionIdService = transactionIdService;
	}
	
	
	public static String getUpdatedAccNumber(String lastAccNumber)
	{
		long lastAccNo = Long.parseLong(lastAccNumber) + 1;
		return lastAccNo+"";
	}
	
	public static Date getNextDateBasedOnNoOfDays(String dateFormatStr)
	{
		try 
		{
			if (dateFormatStr!=null && dateFormatStr.trim().length() > 0)
			{
				System.out.println("--- NO DATE FORMAT FOUND ----");
				return null;
			}
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Using today's date
			c.add(Calendar.DATE, 5); // Adding 5 days
			String output = sdf.format(c.getTime());
			System.out.println(output);
			
			Date nextDate = sdf.parse(output);
			
			return nextDate;
		}
		catch (Exception e) {
		}
		return null;
	}
	
	public static String generateOTP(int size) throws NoSuchAlgorithmException
	{
		StringBuilder generatedToken = new StringBuilder();
		SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
		// Generate 20 integers 0..20
		for (int i = 0; i < size; i++) 
		{
			generatedToken.append(number.nextInt(9));
		}
		return generatedToken.toString();
	}
	
	public static String generateAndStoreOtp(int size) throws Exception 
	{
		String otp = generateOTP(6);
		System.out.println("GENERATED OTP " + otp);
		amsLogger.writeInfoLog("GENERATED OTP::"+otp); 

		return otp;
	}
	
	public static String generateHash(String toHash) throws Exception 
	{
		MessageDigest md = null;
		byte[] hash = null;
		md = MessageDigest.getInstance("SHA-512");
		hash = md.digest(toHash.getBytes("UTF-8"));
		return convertToHex(hash);
	}
	
	public static String generateMd5(String input) 
	{
		String hashtext = null;
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(input);
			oos.flush();
			
			byte[] messageDigest = md.digest(bos.toByteArray());
			hashtext = convertToHex(messageDigest);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return hashtext;
	}

	public static String convertToHex(byte[] raw) 
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < raw.length; i++) 
		{
			sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public static String getBase64Image(String imageFileLocation) 
	{
		try 
		{
			//File file = new File(imageFileLocation);
			//new File("E:\\AMS\\Speta\\logo\\cbimage.png")
			//E:\\KYC_IMAGE\\adharCard.png
			
			File file = new File("E:\\KYC_IMAGE\\adharCard.png");
			
			if (!file.exists())
			{
				return null;
			}
			
			byte[] fileContent = FileUtils.readFileToByteArray(file);		
			String encodeString = Base64.getEncoder().encodeToString(fileContent);
			return encodeString;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map getCustomerIdMap()
	{
		String customerID = null;
		
		Map<String, String> CustomerIdMapValues = new HashMap<String, String>(); 
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) {
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) {
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			CustomerIdMapValues.put("julianYear", year);
			CustomerIdMapValues.put("julianDate", julianDayFormat);
			
			String Date = dateFormat.format(new Date());
			
			String custIdvalue = customerMasterService.getCustId(year, julianDayFormat);
			if(custIdvalue == null) 
			{
				 customerID = year + "0000001" + julianDayFormat;
				 CustomerIdMapValues.put("action", "A");
				
			}
			else 
			{ 
				String customerIdIncremnt = (custIdvalue.substring(2, (custIdvalue.length() - 3)) + 1);
				customerID = year + customerIdIncremnt + julianDayFormat;
				CustomerIdMapValues.put("action", "U");
				
			}	   
			CustomerIdMapValues.put("cust_id", customerID );
			return CustomerIdMapValues;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static int updateCustID(CustomerIdCreation customerIdCreation)
	{
		return customerMasterService.updateCustid(customerIdCreation);
		
	}
	
	@SuppressWarnings("unused")
	public static EmailTemplate getEmailTemplateForSendMail(String fromMailId, String toMailId, String subject, String bodyMessage) 
	{
		EmailTemplate emailTemplate = new EmailTemplate();
		try 
		{
			if (fromMailId == null || fromMailId == "")
			{
				fromMailId = "contactus@AMStechnologies.com";
			}
			
			emailTemplate.setStrFrom(fromMailId);
			emailTemplate.setStrTo(toMailId);
			emailTemplate.setStrSubject(subject);
			emailTemplate.setStrText(bodyMessage);
			return emailTemplate;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static EmailTemplate getEmailTemplateForSendMailToMultiple(String fromMailId, ArrayList<String> toMailId, String subject, String bodyMessage) 
	{
		EmailTemplate emailTemplate = new EmailTemplate();
		try 
		{
			if (fromMailId == null || fromMailId == "")
			{
				fromMailId = "contactus@AMStechnologies.com";
			}
			
			emailTemplate.setStrFrom(fromMailId);
			emailTemplate.setStrToList(toMailId);
			emailTemplate.setStrSubject(subject);
			emailTemplate.setStrText(bodyMessage);
			return emailTemplate;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getGeneratedTransactionId() 
	{
		String transactionId = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try 
		{
			String year = String.valueOf((LocalDateTime.now().getYear()) % 100);//23
			String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());//37
			
			StringBuilder julianDayFormatSb = new StringBuilder("");
			
			if (dayOfCurrentYearinStr.length() == 1) {
				julianDayFormatSb.append("00").append(dayOfCurrentYearinStr);
			}
			else if (dayOfCurrentYearinStr.length() == 2) {
				julianDayFormatSb.append("0").append(dayOfCurrentYearinStr);
			}
			else
			{
				julianDayFormatSb.append(dayOfCurrentYearinStr);
			}
			String julianDayFormat = julianDayFormatSb.toString();
			
			String Date = dateFormat.format(new Date());
			
			List<TransactionIdTable> transactionIdTables = transactionIdService.getTransactionIdList(year, julianDayFormat);
			TransactionIdTable transactionIdTable = null;
			
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
				//String tranId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				transactionId = String.valueOf(Long.parseLong(transactionIdTable.getStrLastTxnSerialNo()) + 1);
				//transactionId = tranId;
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

	public static double stringToDouble(String value) {
		double convertValue = Double.parseDouble(value);
		return convertValue;
	}
	
	public static int stringToInt(String value)
	{
		int convertValue = Integer.parseInt(value);
		return convertValue;
	}
	
	public static Date getCurrentDate() 
	{
		return new Date();
	}
	
	public static Date getCurrentFormattedDate() throws Exception 
	{
		Date curretDate = new Date();		
		String strCurrentDate = simpleDateFormat.format(curretDate);		
		return simpleDateFormat.parse(strCurrentDate);
	}
	
	public static java.sql.Date getCurrentSqlDate() 
	{
		return new java.sql.Date(new Date().getTime());
	}
	
	public static java.sql.Time getCurrentSqlTime() 
	{
		return new java.sql.Time(new Date().getTime());
	}
	
	public static Time getFormattedCurrentTime() throws Exception 
	{
		String timeFormatdate = simpleTimeFormat.format(new Date());
		return new java.sql.Time(simpleTimeFormat.parse(timeFormatdate).getTime());
	}
	
	public static String getStrTime() throws Exception 
	{
		Date curretDate = new Date();
		String timeFormatdate = simpleTimeFormat.format(curretDate);
		Date formateTimeDate = simpleTimeFormat.parse(timeFormatdate);
		return String.valueOf(formateTimeDate.getTime());
	}
	public static String getAlphaNumericString()
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rd = new Random();
		int number = rd.nextInt(10000);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2; i++)
		{
			int index=(int)(AlphaNumericString.length()* Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		sb.append(number);
		return sb.toString();
	}
	public static String getAlphaNumericAuditId()
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rd = new Random();
		long number = rd.nextInt(100000000);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3; i++)
		{
			int index=(int)(AlphaNumericString.length()* Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		sb.append(number);
		return sb.toString();
	}
	
	public static String getLocalDate() {
		LocalDateTime myDate = LocalDateTime.now();
		
		DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
		String formattedDate = myDate.format(myDateFormatter);
		SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
		return formattedDate;
	}
	
	public static String getResponseMTI(String requestMti) 
	{
		Map<String, String> mtiDataMap = new HashMap<String, String>();
		mtiDataMap.put("0200", "0210");
		mtiDataMap.put("0400","0410");		
		return mtiDataMap.get(requestMti);
	}
	
	public static String getGlLoadingAccountNumber(String loadChannel) 
	{
		Map<String, String> glAccountNoMap = new HashMap<String, String>();
		glAccountNoMap.put("NLB", "999120000002");
		glAccountNoMap.put("ULB","999120000001");		
		glAccountNoMap.put("CLB","999120000003");
		return glAccountNoMap.get(loadChannel);
	}
	
	public static String getBillingYearAndMonthAndDay(String strBillingDate) 
	{
		StringBuilder result = new StringBuilder();
		try 
		{
			int billingDate = Integer.parseInt(strBillingDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			
			int day = cal.get(Calendar.DAY_OF_MONTH);			
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			result.append(""+year).append("-");
			
			if (billingDate < day) 
			{
				month = month + 1;
			}
			
			String strMonth = String.valueOf(month);			
			if (strMonth.length() != 2) 
			{
				strMonth = "0" + strMonth;
			}
			if (strBillingDate.length() != 2)
			{
				strBillingDate = "0" + strBillingDate;
			}
			System.out.println("strMonth::["+strMonth+"]");
			
			result.append(strMonth).append("-").append(strBillingDate);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	public static Date getPaymentDueDate(String curDate, int nextDaysCount) 
	{
		try 
		{
			final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			final Date date = format.parse(curDate);
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, nextDaysCount);
			return calendar.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Date getNextDateBasedOnCount(String curDate, int nextDaysCount, SimpleDateFormat format) 
	{
		try 
		{
			//SimpleDateFormat format = null
			if (format == null) 
			{
				format = new SimpleDateFormat("yyyy-MM-dd");
			}
			
			Date date = format.parse(curDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, nextDaysCount);
			return calendar.getTime();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//Added by Pankaj Pawar for 18 Digit alphanumeric code as Reference number
	public static String getRefNo()
	{
		String refNo = "";
		try
		{
			Random rd = new Random();
			int number = rd.nextInt(100);
			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 3; i++)
			{
				int index=(int)(AlphaNumericString.length() * Math.random());
				sb.append(AlphaNumericString.charAt(index));
			}
			sb.append(number);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
			String cuurentDateStr = sdf.format(new Date());
			Date currentdate = sdf.parse(cuurentDateStr);
			String getNumericValue = String.valueOf(currentdate.getTime());
			sb.append(getNumericValue);
			refNo = sb.toString();
			System.out.println("refNo:::"+refNo);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return refNo;
	}
	
	public static String getFormattedDateTimeStr() 
	{
		try 
		{
			SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd MMM YYYY hh:mm:ss");
			return simpleDateFormat3.format(new Date());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getCurrentDbDateStr() 
	{
		String strCurrentDate = null;
		try 
		{
			Date curreDate = new Date();
			strCurrentDate = simpleDateFormat3.format(curreDate);
			System.out.println("strCurrentDate::"+strCurrentDate);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return strCurrentDate;
	}
	
	public static String getExtendedTimeInStringFromCuurentTime(int extendedTimeData) 
	{
		try 
		{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			
			String currentTime = df.format(new Date());			
			Date currentTM = df.parse(currentTime); 
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentTM);
			cal.add(Calendar.MINUTE, extendedTimeData);
			String newTime = df.format(cal.getTime());
			
			//System.out.println("newTime::"+newTime);
			
			return newTime;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNibssTxnId() 
	{
		StringBuilder nibsstxnIdSb = new StringBuilder();
		try
		{
			nibsstxnIdSb.append("000068");//client code
			
			SimpleDateFormat sdf = new SimpleDateFormat("YYMMddhhmmss");
			Date curDate = new Date();
			String curDateStr = sdf.format(curDate);
			nibsstxnIdSb.append(curDateStr);
			
			StringBuilder generatedToken = new StringBuilder();
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < 12; i++) 
			{
				generatedToken.append(number.nextInt(9));
			}
			nibsstxnIdSb.append(generatedToken);
			System.out.println("nibss txnId::"+nibsstxnIdSb);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return nibsstxnIdSb.toString();
	}
	
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
	public static ArrayList<String> getGroupMailId() 
	{
		ArrayList<String> toMailIdList = new ArrayList<String>();
		
		/*
		toMailIdList.add("sachin.koti@AMStechnologies.com");
		toMailIdList.add("brana@AMStechnologies.com");
		toMailIdList.add("ptayde@AMStechnologies.com");
		toMailIdList.add("skhawse@AMStechnologies.com");
		toMailIdList.add("atamrakar@AMStechnologies.com");
		*/
		toMailIdList.add("ssoni@AMStechnologies.com");
		
		return toMailIdList;
	}
	
	public static class DenominationValues
	{
		public static final Long D10 =  10l;
		public static final Long D20 =  20l;
		public static final Long D50 =  50l;
		public static final Long D100 =  100l;
		public static final Long D200 =  200l;
		public static final Long D500 =  500l;
		public static final Long D1000 =  1000l;
		public static final Long D2000 =  2000l;
	}
	
	public static class AccountMstPrefix 
	{
		public static final String ACCOUNT_BALANCE = "ABAL";
		public static final String ACCOUNT_EAR_MARK_AMOUNT = "AEMA";
		public static final String ACCOUNT_PRE_CRED_AMOUNT = "APCA";
		public static final String ACCOUNT_DAILY_LIMIT = "ADL";
		public static final String ACCOUNT_MONTHLY_LIMIT = "AML";
		public static final String ACCOUNT_YEARLY_LIMIT = "AYL";
	}
	
	public static class TierAccountMstPrefix 
	{
		public static final String TIER_ACCOUNT_DAILY_LIMIT = "TADL";
	}
	public static String getReduceAmount(String existingAmount, String txnAmount) 
	{
		String updatableFieldValue = null;
		try 
		{
			double existingAmt = Double.parseDouble(existingAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingAmt - txnAmt;
			
			updatableFieldValue = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) 
		{
			System.out.println("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatableFieldValue;
	}
	public static String getIncreaseAmount(String existingAmount, String txnAmount) 
	{
		String updatableFieldValue = null;
		try 
		{
			double existingAmt = Double.parseDouble(existingAmount);
			double txnAmt = Double.parseDouble(txnAmount);
			
			double totalAmt = existingAmt + txnAmt;
			
			updatableFieldValue = Utils.decimalFormat.format(totalAmt);
		}
		catch (Exception e) 
		{
			System.out.println("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return updatableFieldValue;
	}
	
	public static String getNewGeneratedSecretCode(String appName, String suffixName) 
	{
		String result = "";
		try 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("DDMMYYYYHHMM");
			String currentDate = dateFormat.format(new Date());			
			
			result = appName + "_" + currentDate + "_" + suffixName;
			
			result = result.trim();
			
			System.out.println("result=["+result+"]");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//Created IV Phrase 
	
	public static String getRandomStr(int length) 
	{
		RandomStringGenerator gen = new RandomStringGenerator(length);
		return gen.nextString();
	}
	
	public static String getIV(String pharse, String separtor) 
	{
		String randomStr = getRandomStr(32);		
		StringBuilder sb = new StringBuilder(pharse);		
		sb.append(separtor).append(randomStr);		
		return sb.toString();
		
	}
	public static String getPhrase() 
	{
		return RandomStringUtils.randomAlphanumeric(8);
	}
	public static String getIv() 
	{
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return Hex.encodeHexString(salt);
	}
	public static String getSalt() 
	{
		return AesUtil.random(16);
	}
	
	public static String getSecretKey(String apiKey, String separator) 
	{
		String randomStr = getRandomStr(6);
		
		StringBuilder sb = new StringBuilder(randomStr);		
		sb.append(separator).append(apiKey);
		
		return sb.toString().toUpperCase();
	}
	
	public static String getJulianDate() 
	{
		String dayOfCurrentYearinStr = String.valueOf(LocalDateTime.now().getDayOfYear());		
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
		return julianDayFormatSb.toString();
	}
	
	public static String getJulianYear() {
		return String.valueOf((LocalDateTime.now().getYear()) % 100);
	}
	
	public static String getIncrementedValue(String value) 
	{
		String result = "";
		int valLen = value.length();
		
		long val = Long.parseLong(value);	//0000	
		val = val + 1;//12
		
		String incrementedValStr = String.valueOf(val);
		int incrementedValLength = incrementedValStr.length();
		
		int remainingLen = value.length();
		if (valLen > incrementedValLength) 
		{
			remainingLen = value.length() - incrementedValStr.length();
			result = value.substring(0, remainingLen) + incrementedValStr;
		}
		else
		{
			result = incrementedValStr;
		}
		return result;
	}
	public static String getRandomAlphabet(int size) 
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++)
		{
			int index=(int)(AlphaNumericString.length()* Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}
	public static Double getTxnAmount(TransactionPostingConfig transactionPostingConfig) 
	{
		double result = 0d;
		try 
		{
			double txnAmount = Utils.stringToDouble(transactionPostingConfig.getTxnAmount());			
			if (transactionPostingConfig.getFee() != null && transactionPostingConfig.getFee().trim().length() > 0) 
		    {
		    	txnAmount = txnAmount + Utils.stringToDouble(transactionPostingConfig.getFee().trim());
		    	if (transactionPostingConfig.getVat() != null && transactionPostingConfig.getVat().trim().length() > 0) 
		    	{
		    		txnAmount = txnAmount + Utils.stringToDouble(transactionPostingConfig.getVat().trim());
		    	}
		    }
			result = txnAmount;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	public static Double getUpdatedTxnAmount(TransactionConfig transactionConfig) 
	{
		double result = 0d;
		try 
		{
			double txnAmount = Utils.stringToDouble(transactionConfig.getTxnAmount());			
			if (transactionConfig.getFee() != null && transactionConfig.getFee().trim().length() > 0) 
		    {
		    	txnAmount = txnAmount + Utils.stringToDouble(transactionConfig.getFee().trim());
		    	if (transactionConfig.getVat() != null && transactionConfig.getVat().trim().length() > 0) 
		    	{
		    		txnAmount = txnAmount + Utils.stringToDouble(transactionConfig.getVat().trim());
		    	}
		    }
			result = txnAmount;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	public static Date getCurrentDateFormat() {
		Date date = new Date();
		return date;
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
	
	public static int daysBetweenDates(String date1, String date2) 
	{
		LocalDate dt1 = LocalDate.parse(date1);
		LocalDate dt2= LocalDate.parse(date2);

		long diffDays = ChronoUnit.DAYS.between(dt1, dt2);
		return Math.abs((int)diffDays);
	}
	public static java.sql.Date getSqlDate(Date dte) 
	{
		return new java.sql.Date(dte.getTime());
	}
	
}
