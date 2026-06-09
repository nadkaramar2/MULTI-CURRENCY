package ams.cms.api.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.dao.AddressProofDocumentTypeDao;
import ams.cms.api.dao.IdentityProofDocumentTypeDao;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.CountryCodeMaster;
import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.PreAccountMasterTemp;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.api.model.QrInfo;
import ams.cms.api.model.UsersPassword;
import ams.cms.api.service.AddressProofDocumentTypeService;
import ams.cms.api.service.CountryCodeMasterService;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.IdentityProofDocumentTypeService;
import ams.cms.api.service.ImageService;
import ams.cms.api.service.NubanCodeConfigService;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.api.service.PreAccountMasterTempService;
import ams.cms.api.service.PreSubAccountMasterService;
import ams.cms.api.service.QrCodeService;
import ams.cms.api.service.UsersPasswordService;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.CustomerIdCreationConfig;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.dao.AccountCreditLimitCategoryDao;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.messaging.SendEmail;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.model.AccountKycDetails;
import ams.cms.model.AccountTransactionLimitation;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;
import ams.cms.model.CustomerIdTable;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AcTypeLrsTcsMasterService;
import ams.cms.services.AccountKycDetailsService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTransactionLimitationService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.services.CustomerIdTableService;
import ams.cms.services.MultiCurrencyFinancialYearMasterService;
import ams.cms.txn.handler.ExternalServerHandler;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.EncryptDecryptUtil;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@RestController
public class SignUpController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(SignUpController.class);

	private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private PreAccountMasterService preAccountMasterService;

	@Autowired
	private AccountTypeMasterService accountTypeMasterService;

	@Autowired
	private AccountKycDetailsService accountKycDetailsService;

	@Autowired
	private AddressProofDocumentTypeService addressProofDocumentTypeService;

	@Autowired
	private IdentityProofDocumentTypeService identityProofDocumentTypeService;

	@Autowired
	private UsersPasswordService usersPasswordService;

	@Autowired
	private SendEmail sendEmailService;

	@Autowired
	private ExternalServerHandler externalServerHandler;

	// added by ankit on 10-05-2023
	@Autowired
	private CustomerIDCreationService customerIDCreationService;
	
	@Autowired
	private CustomerIdCreationConfig customerIdCreationConfig;

	@Autowired
	private CustomerIdTableService customerIdTableService;
	// added by ankit on 10-05-2023

	@Autowired
	private ImageService imageService;

	@Autowired
	private EncryptDecryptConfig encryptDecryptData;

	@Autowired
	private EncryptDecryptUtil encryptDecryptUtil;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CountryCodeMasterService countryCodeMasterService;

	@Autowired
	private PreSubAccountMasterService preSubAccountMasterService;

	@Autowired
	private CustomerIdService customerIdService;

	@Autowired
	private PreAccountMasterTempService preAccountMasterTempService;

	@Autowired
	private IdentityProofDocumentTypeDao identityProofDocumentTypeDao;

	@Autowired
	private AddressProofDocumentTypeDao addressProofDocumentTypeDao;

	@Autowired
	private Environment environment;
	
	@Autowired
	private AccountTransactionLimitationService accountTransactionLimitationService;
	
	@Autowired 
	private NubanCodeConfigService nubanCodeConfigService;
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@Autowired
	private AccountCreditLimitCategoryDao accountCreditLimitCategoryDao;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	

	
	@Autowired
	private AcTypeLrsTcsMasterService acTypeLrsTcsMasterService;
	
	@Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value = "/verifyMobileNumber", method = RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountInfo(@RequestBody PayloadReqRes req, HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{   
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			
			String token = request.getHeader("Authorization");
			processResponse.setStatus("S0000");
			processResponse.setResult("Success");

			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			// PreAccountMaster preAccountMasterSave =
			// preAccountMasterService.getPreAccountMasterData(preAccountMaster.getStrMobileNo(),
			// preAccountMaster.getStrEmailID());
			PreAccountMaster existingPreAccountData = preAccountMasterService.getPreAccountInfo(preAccountMaster.getStrMobileNo());

			if (existingPreAccountData != null)
			{
				if (existingPreAccountData.getStrPassword() != null && existingPreAccountData.getStrPassword().trim().length() > 0) 
				{
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("User Already Registered!!");
				}
				else 
				{
					makeTemporaryRegistrationEntry(existingPreAccountData);

					Date birthDate = dt.parse(preAccountMaster.getStrDOB());
					preAccountMaster.setBirthDate(birthDate);
					preAccountMaster.setStrDateOfCreation(new Date());
					preAccountMaster.setStrCreatedBy(preAccountMaster.getStrFirstName());

					preAccountMaster.setStrJwtToken(token);

					preAccountMaster.setStrAccountCreatedFlag("N");
					preAccountMaster.setIsCustIdCreated("N");
					preAccountMaster.setStrIsKycVerified("N");
					

					String countryCodeReqData = preAccountMaster.getStrCountryCode();
					if (countryCodeReqData != null && countryCodeReqData.trim().length() > 0) {
						String[] countryCodeArr = countryCodeReqData.split("-");
						if (countryCodeArr != null && countryCodeArr.length == 2) {
							preAccountMaster.setStrCountryCode(countryCodeArr[0]);
							preAccountMaster.setStrCountryCodeShortName(countryCodeArr[1]);
						}
					} else {
						preAccountMaster.setStrCountryCode(existingPreAccountData.getStrCountryCode());
						preAccountMaster.setStrCountryCodeShortName(existingPreAccountData.getStrCountryCodeShortName());
					}

					// int update = preAccountMasterService.updateSignUpdata(preAccountMaster);
					int update = preAccountMasterService.updatePreAccountExistingInfoData(preAccountMaster);
					if (update > 0) {
						String mobile_no = preAccountMaster.getStrMobileNo();
						String countryCode = preAccountMaster.getStrCountryCode();
						mobile_no = countryCode + mobile_no;

						String otp = Utils.generateAndStoreOtp(6);
						int resultCount = preAccountMasterService.updateSignUpData(preAccountMaster,
								Utils.generateHash(otp));
						if (resultCount > 0) {
							String smsOTP = otp.substring(0, 3);
							String emailOTP = otp.substring(3);
							int otpResponse = this.processToSendOtp(smsOTP, emailOTP, mobile_no,
									preAccountMaster.getStrEmailID());
							if (otpResponse > 0) {
								processResponse.setStatus("S0000");
								processResponse.setResult("Success");
								processResponse.setMessage("OTP Sent successfully on your mentioned mobile no.");
							} else {
								processResponse.setStatus("E0000");
								processResponse.setResult("Error");
								processResponse.setMessage("Issue occurs while sending OTP!!!");
							}
						}
					} else {
						processResponse.setStatus("E0000");
						processResponse.setResult("Failure");
						processResponse.setMessage("New Record Addition Failed.");
					}
				}
			} else {
				Date birthDate = dt.parse(preAccountMaster.getStrDOB());
				preAccountMaster.setBirthDate(birthDate);
				preAccountMaster.setStrDateOfCreation(new Date());
				preAccountMaster.setStrCreatedBy(preAccountMaster.getStrFirstName());

				preAccountMaster.setStrJwtToken(token);
				preAccountMaster.setStrAccountCreatedFlag("N");
				preAccountMaster.setIsCustIdCreated("N");
				preAccountMaster.setStrIsKycVerified("N");

				String countryCodeReqData = preAccountMaster.getStrCountryCode();

				String[] countryCodeArr = countryCodeReqData.split("-");
				if (countryCodeArr != null && countryCodeArr.length == 2) {
					preAccountMaster.setStrCountryCode(countryCodeArr[0]);
					preAccountMaster.setStrCountryCodeShortName(countryCodeArr[1]);
				}

				preAccountMaster = preAccountMasterService.saveSignUpData(preAccountMaster);

				String mobile_no = preAccountMaster.getStrMobileNo();
				String countryCode = preAccountMaster.getStrCountryCode();
				mobile_no = countryCode + mobile_no;

				// int otp = this.processToSendOtp(mobile_no);

				String otp = Utils.generateAndStoreOtp(6);
				int resultCount = preAccountMasterService.updateSignUpData(preAccountMaster, Utils.generateHash(otp));
				if (resultCount > 0) {
					String smsOTP = otp.substring(0, 3);
					String emailOTP = otp.substring(3);
					int otpResponse = this.processToSendOtp(smsOTP, emailOTP, mobile_no,
							preAccountMaster.getStrEmailID());
					if (otpResponse > 0) {
						processResponse.setStatus("S0000");
						processResponse.setResult("Success");
						processResponse.setMessage("OTP Sent successfully on your mentioned mobile no.");
					} else {
						processResponse.setStatus("E0000");
						processResponse.setResult("Error");
						processResponse.setMessage("Issue occurs while sending OTP.");
					}
				} else {
					processResponse.setStatus("E0000");
					processResponse.setResult("Error");
					processResponse.setMessage("Please check contact details");
				}
			}
		} catch (Exception e) {
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error During SignUp");
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	private void makeTemporaryRegistrationEntry(PreAccountMaster preAccountMaster)
	{
		try {
			PreAccountMasterTemp preAccountMasterTemp = new PreAccountMasterTemp();

			preAccountMasterTemp.setStrMobileNo(preAccountMaster.getStrMobileNo());
			preAccountMasterTemp.setBirthDate(preAccountMaster.getBirthDate());
			preAccountMasterTemp.setStrDateOfCreation(preAccountMaster.getStrDateOfCreation());
			preAccountMasterTemp.setStrCountryCode(preAccountMaster.getStrCountryCode());
			preAccountMasterTemp.setStrFirstName(preAccountMaster.getStrFirstName());
			preAccountMasterTemp.setStrMiddleName(preAccountMaster.getStrMiddleName());
			preAccountMasterTemp.setStrLastName(preAccountMaster.getStrLastName());
			preAccountMasterTemp.setStrOtp(preAccountMaster.getStrOtp());
			preAccountMasterTemp.setStrJwtToken(preAccountMaster.getStrJwtToken());
			preAccountMasterTemp.setStrCountryCodeShortName(preAccountMaster.getStrCountryCodeShortName());
			preAccountMasterTemp.setStrCreatedBy(preAccountMaster.getStrCreatedBy());
			preAccountMasterTemp.setStrEmailID(preAccountMaster.getStrEmailID());
			preAccountMasterTemp.setStrGender(preAccountMaster.getStrGender());
			preAccountMasterTemp.setStrID(preAccountMaster.getStrID());

			preAccountMasterTempService.saveSignUpDataTemporary(preAccountMasterTemp);
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}

	// Api to Send Otp called internally with VerifyMobileNumber Api
	// Added By Jyoti S
	// @RequestMapping(value = "/sendOtp", method = RequestMethod.POST)
	// public ResponseEntity<?> processToSendOtp(String mobile_no)
	// public int processToSendOtp(String mobile_no) {
	public int processToSendOtp(String smsOTP, String emailOTP, String mobileNo, String emailId) 
	{
		try 
		{
			String message = "Dear User, Your one time password is " + smsOTP + ".Please enter the OTP to proceed."
					+ "Powered by AMS Technologies Pvt Ltd";

			// Dear User, Your one time password is 321.Please enter the OTP to proceed.
			// Powered by AMS Technologies Pvt Ltd

			// Added changes for Nigeria User SMS [END]
			amsLogger.writeInfoLog("processToSendOtp:: mobileNo=[" + mobileNo + "] smsOTP=[" + smsOTP + "]");
			if (mobileNo.startsWith("+234"))
			{
				amsLogger.writeInfoLog("Sending SMS for Nigeria USERS.............");
				TransactionConfig transactionConfig = new TransactionConfig();
				transactionConfig.setCode("S0000");

				// mobileNo = mobileNo.substring(1);
				List<String> toNumbersListforSms = new ArrayList<String>();
				toNumbersListforSms.add(mobileNo);

				message = "Dear User, Your one time password is " + smsOTP + ".Please enter the OTP to proceed.";

				transactionConfig.setSmsMessage(message);
				transactionConfig.setToNumbersListforSms(toNumbersListforSms);

				transactionConfig = externalServerHandler.sendOtpSms(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				{
					amsLogger.writeInfoLog("--------- SMS SENT TO Nigeria USERS --------------");
					amsLogger.writeInfoLog("emailOTP::[" + emailOTP + "] emailId=[" + emailId + "]");
					String responseStr = sendEmailService.sendEmailCustomer(emailId, emailOTP);
					if (responseStr.indexOf("Sent") != -1) 
					{
						return 1;
					}
				}
				return 0;
			}
			// Added changes for Nigeria User SMS [END]
			String smsUrl = "";
			// if (mobileNo.indexOf("+91")!=-1)
			if (mobileNo.indexOf("91") != -1) 
			{
				// mobileNo = mobileNo.substring(3);
				smsUrl = environment.getProperty("ams.sms.local.url");
			} 
			else 
			{
				smsUrl = environment.getProperty("ams.sms.global.url");
			}
			smsUrl = smsUrl.trim();
			amsLogger.writeInfoLog("smsUrl::[" + smsUrl + "]");

			String data = "";
			data += "username=" + URLEncoder.encode("di78-AMS", "UTF-8");
			data += "&password=" + URLEncoder.encode("digimile", "UTF-8");
			data += "&type=0";
			data += "&dlr=1";
			// data += "&destination=" + preAccountMaster.getStrMobileNo();
			data += "&destination=" + mobileNo;
			data += "&source=" + URLEncoder.encode("AMS", "UTF-8");
			data += "&message=" + URLEncoder.encode(message, "UTF-8");
			;
			data += "&entityid=1101363910000016951";
			data += "&tempid=1107165285867522889";

			// URL url = new URL("http://sms.digimiles.in/bulksms/bulksms?" + data);
			URL url = new URL(smsUrl + "?" + data);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) 
			{
				buffer.append(line).append("\n");
			}

			rd.close();
			conn.disconnect();

			// amsLogger.writeInfoLog("sendSms - sending Buffer" + buffer);

			if (buffer.length() > 4) 
			{
				String processingMsg = buffer.toString();
				sendEmailService.sendEmailCustomer(emailId, emailOTP);

				// amsLogger.writeInfoLog("sendSms - processingMsg--" + processingMsg);

				String[] respElement = processingMsg.split("\\|");

				if (null != respElement && respElement.length > 1) {
				}
				return 1;
			} 
			else 
			{
				return 0;
			}

			// }
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}

	// Resend Otp Api Added by Jyoti Shirahatti
	@RequestMapping(value = "/reSendOtp", method = RequestMethod.POST)
	public ResponseEntity<?> processToSendOtp(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try {

			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			PreAccountMaster preAccountMasterMobileData = preAccountMasterService.getPreAccountMasterDataByMobileNumber(preAccountMaster.getStrMobileNo());
			String otp = Utils.generateAndStoreOtp(6);
			preAccountMasterService.updateSignUpData(preAccountMasterMobileData, Utils.generateHash(otp));

			String otp1 = otp.substring(0, 3);
			String otp2 = otp.substring(3);

			String template = "Dear User, Your one time password is " + otp1 + ".Please enter the OTP to proceed."
					+ "Powered by AMS Technologies Pvt Ltd";

			// Dear User, Your one time password is 321.Please enter the OTP to proceed.
			// Powered by AMS Technologies Pvt Ltd

			String data = "";
			data += "username=" + URLEncoder.encode("di78-AMS", "UTF-8");
			data += "&password=" + URLEncoder.encode("digimile", "UTF-8");
			data += "&type=0";
			data += "&dlr=1";
			data += "&destination=" + preAccountMasterMobileData.getStrMobileNo();
			data += "&source=" + URLEncoder.encode("AMS", "UTF-8");
			data += "&message=" + URLEncoder.encode(template, "UTF-8");
			;
			data += "&entityid=1101363910000016951";
			data += "&tempid=1107165285867522889";

			URL url = new URL("http://sms.digimiles.in/bulksms/bulksms?" + data);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			rd.close();
			conn.disconnect();
			// amsLogger.writeInfoLog("sendSms - sending Buffer" + buffer);

			if (buffer.length() > 4) {
				String processingMsg = buffer.toString();
				sendEmailService.sendEmailCustomer(preAccountMasterMobileData.getStrEmailID(), otp2);
				// amsLogger.writeInfoLog("sendSms - processingMsg--" + processingMsg);

				String[] respElement = processingMsg.split("\\|");

				if (null != respElement && respElement.length > 1) {
				}
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("OTP ReSent successfully on your mentioned mobile no.");
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Error");
				processResponse.setMessage("Please check contact details");
			}

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}

		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// Verify Otp Api added by Jyoti S
	@RequestMapping(value = "/verifyOtp", method = RequestMethod.POST)
	public ResponseEntity<?> processToVerifyOtp(@RequestBody PayloadReqRes req, HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try {
			// String token = request.getHeader("Authorization");
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			PreAccountMaster preAccountMasterMobileData = preAccountMasterService.getPreAccountMasterDataByMobileNumber(preAccountMaster.getStrMobileNo());
			String hashedOtp = Utils.generateHash(preAccountMaster.getStrOtp().trim());
			
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+preAccountMaster.getStrOtp()+" size is "+preAccountMaster.getStrOtp().length()); 

			// if (token.equalsIgnoreCase(preAccountMasterMobileData.getStrJwtToken()))
			// {
			if (hashedOtp.equalsIgnoreCase(preAccountMasterMobileData.getStrOtp())) {
				preAccountMasterService.updateSignUpData(preAccountMasterMobileData, hashedOtp);

				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Otp Verified Successfully");
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Otp Not Matched");
			}
			// }
			/*
			 * else { processResponse.setStatus("E0000");
			 * processResponse.setResult("Failure");
			 * processResponse.setMessage("Token Not Matched"); }
			 */
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));

			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}

		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// Api to get list of accountType for drop down by Jyoti S
	@RequestMapping(value = "/accountCategoryList", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> processGetAccountCategoryList(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<AccountTypeMaster> listData = accountTypeMasterService.getAllAccountTypeMasterList();
			List<String> accountType = new ArrayList<String>();
			for (AccountTypeMaster accountTypeMaster : listData) {
				// accountType.add(accountTypeMaster.getStrAccountType());
				accountType.add(accountTypeMaster.getStrAccountType() + "-" + accountTypeMaster.getStrDescription());
			}
			if (!(accountType.isEmpty())) {
				processResponse.setAccountType(accountType);
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Account Data fetched Successfully");
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Error");
				processResponse.setMessage("No data found");
			}

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));

			processResponse.setStatus("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
			processResponse.setResult("Error");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/setPassword", method = RequestMethod.POST)
	public ResponseEntity<?> setPasswordApi(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try {
			amsLogger.writeInfoLog("Inside setPassword application Name=[" + CommonConstants.applicationName + "]");
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) {
				//ProcessResponse setPasswordNigeriaRegistration = setPasswordWithNigeriaRegistration(preAccountMaster); //commented becuz participant was not sent to be setted in the method 
				ProcessResponse setPasswordNigeriaRegistration = setPasswordWithNigeriaRegistration(preAccountMaster,participantId);
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, setPasswordNigeriaRegistration));
			}

			boolean isExist = preAccountMasterService.isAccountExist(preAccountMaster);
			if (!isExist) {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("User Account Not Found.");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}

			if (preAccountMaster.getStrPassword() != null && preAccountMaster.getStrPassword().trim().length() > 0) {
				String encyptedPassword = encryptDecryptUtil.encrypt(preAccountMaster.getStrPassword());
				preAccountMaster.setStrPassword(encyptedPassword);

				UsersPassword usersPassword = new UsersPassword();
				usersPassword.setStrMobileNo(preAccountMaster.getStrMobileNo());
				usersPassword.setStrNewPassword(encyptedPassword);

				int id = usersPasswordService.addUsersPassword(usersPassword);
				if (id > 0) {
					preAccountMaster.setStrIsAccountNoCreated("N");
					preAccountMaster.setIsCustIdCreated("N"); // Customer id not created flag
					preAccountMaster.setStrAccountCreatedFlag("Y");// After Completing Sign up
					preAccountMaster.setStrIsKycVerified("N");
					preAccountMaster.setStrDateOfCreation(Utils.getCurrentDate()); // New Added By Sunny Soni
					// int result = preAccountMasterService.updatePassword(preAccountMaster);
					int result = preAccountMasterService.updatePreAccountMasterBasedOnParameter(preAccountMaster);
					if (result > 0) {
						// Added by Sunny soni for regarding mail change Start
						try {
							PreAccountMaster preAccountMasterInfo = preAccountMasterService.getPreAccountInfo(preAccountMaster.getStrMobileNo());
							if (preAccountMasterInfo != null) {
								StringBuilder bodyMsg = new StringBuilder("Dear User, ");
								bodyMsg.append("<br/><br/>");
								bodyMsg.append("There is a request for New Customer registration by Mr/Mrs "
										+ preAccountMasterInfo.getStrFirstName() + " "
										+ preAccountMasterInfo.getStrLastName() + " on "
										+ Utils.getFormattedDateTimeStr() + "");
								bodyMsg.append("<br/>");
								bodyMsg.append(
										"Kindly log into Back-office and authorise the request by verifying KYC to allocate Cust ID.");
								bodyMsg.append("<br/><br/><br/>");
								bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
								String subject = "Regarding to Customer ID";
								sendMailToMultiplePerson(bodyMsg.toString(), subject);
							}
						} catch (Exception e) {
							amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
						}
						// Added by Sunny soni for regarding mail change End

						processResponse.setStatus("S0000");
						processResponse.setResult("Success");
						processResponse.setMessage("Password Added Successfully.");
					} else {
						processResponse.setStatus("E0000");
						processResponse.setResult("Failure");
						processResponse.setMessage("Password not added properly.");
					}
				}
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Password Can Not Be Blank.");
			}
		} catch (Exception e) {
			processResponse.setStatus("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
			processResponse.setResult("Error");
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// Added by Prashant Tyade For change password Start
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<?> processToChangePassword(@RequestBody PayloadReqRes req) {
		ProcessResponse processResponse = new ProcessResponse();
		try {
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			UsersPassword usersPassword = new ObjectMapper().readValue(decrypt, UsersPassword.class);
			usersPassword = new ObjectMapper().readValue(decrypt, UsersPassword.class);

			PreAccountMaster preAccountMaster = new PreAccountMaster();
			preAccountMaster.setStrMobileNo(usersPassword.getStrMobileNo());

			PreAccountMaster preAccouMasterObj = preAccountMasterService
					.getPreAccountInfo(preAccountMaster.getStrMobileNo());
			if (preAccouMasterObj != null && preAccouMasterObj.getStrID() != null) {
				String existingPasswordDB = preAccouMasterObj.getStrPassword();// Getting Existing Password (already in
																				// Database).

				String userEnteredOldPassword = usersPassword.getStrOldPassword(); // User Entered existing password.
				userEnteredOldPassword = encryptDecryptUtil.encrypt(userEnteredOldPassword);

				if (existingPasswordDB.equals(userEnteredOldPassword)) {
					String newPassword = usersPassword.getStrNewPassword(); // USer Entered New Password.
					newPassword = encryptDecryptUtil.encrypt(newPassword);

					int index = checkExistingAvailablePasswordUserWise(usersPassword, newPassword);
					amsLogger.writeInfoLog("Index Val::[" + index + "]");
					if (index == 0) {
						usersPassword.setStrNewPassword(newPassword);
						int id = usersPasswordService.addUsersPassword(usersPassword);
						amsLogger.writeInfoLog("During addUsersPassword id Val::[" + id + "]");
						if (id > 0) {
							preAccountMaster.setStrPassword(newPassword);
							int result = preAccountMasterService.updatePassword(preAccountMaster);
							amsLogger.writeInfoLog("During updatePassword result Val::[" + result + "]");
							if (result > 0) {
								processResponse.setStatus("Success");
								processResponse.setCode("S0000");
								processResponse.setMessage("Password Updated Successfully.");
							} else {
								processResponse.setStatus("Failed");
								processResponse.setCode("E0000");
								processResponse.setMessage("Password Updation Failed");
							}
						}
					} else {
						processResponse.setStatus("Failed");
						processResponse.setCode("E0000");
						processResponse.setMessage(
								"Please Choose Another Password. Your Entered Password matched with previous Password.");
					}
				} else {
					processResponse.setStatus("Failed");
					processResponse.setCode("E0000");
					processResponse.setMessage("Password did not matched. Please enter correct password.");
				}
			} else {
				processResponse.setStatus("Failed");
				processResponse.setCode("E0000");
				processResponse.setMessage("User Account Not Found, Please Enter Correct UserId.");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during changePassword");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<?> processToForgotPassword(@RequestBody PayloadReqRes req, HttpServletRequest request) {
		ProcessResponse processResponse = new ProcessResponse();
		EmailTemplate emailTemplate = new EmailTemplate();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			PreAccountMaster preAccountMaster1 = preAccountMasterService.getPreAccountMasterData(null,
					preAccountMaster.getStrEmailID());

			if (preAccountMaster1 == null) {
				processResponse.setStatus("Failed");
				processResponse.setCode("E0000");
				processResponse.setMessage("Email ID Not Found.");
			} else {
				emailTemplate.setStrFrom("contactus@AMStechnologies.com");
				emailTemplate.setStrTo(preAccountMaster1.getStrEmailID());
				emailTemplate.setStrSubject("Regarding to Forgot Password");
				emailTemplate.setStrText("Dear User, " + "<br/><br/> Your Password is : "
						+ encryptDecryptUtil.decrypt(preAccountMaster1.getStrPassword()) + "<br/><br/><br/>"
						+ "Powered by AMS Technologies Pvt Ltd");

				emailService.sendSimpleHtmlContentMessage(emailTemplate);
				processResponse.setStatus("Success");
				processResponse.setCode("S0000");
				processResponse.setMessage("Please check. Password sent on your registered email.");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	int checkExistingAvailablePasswordUserWise(UsersPassword usersPassword, String newPassword) {
		int index = 0;
		try {
			UsersPassword usrPassword = new UsersPassword();
			usrPassword.setStrMobileNo(usersPassword.getStrMobileNo());

			List<UsersPassword> existingPasswordsList = usersPasswordService.getUserPassword(usrPassword);

			int currentIndex = 0;
			for (UsersPassword usrpass : existingPasswordsList) {
				if (newPassword.equals(usrpass.getStrNewPassword())) {
					index = currentIndex;
					break;
				}
				currentIndex++;
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return index;
	}
	// Added by Prashant Tyade For change password End

	@RequestMapping(value = "/accountTypeStore", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> processToStoreAccountType(@RequestBody PayloadReqRes req) {
		ProcessResponse processResponse = new ProcessResponse();
		try {
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			PreAccountMaster preAccountMasterMobileData = preAccountMasterService
					.getPreAccountMasterDataByMobileNumber(preAccountMaster.getStrMobileNo());

			int i = preAccountMasterService.updateAccountType(preAccountMasterMobileData,
					preAccountMaster.getStrAccountType());
			if (i > 0) {
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Account Type Stored Successfully");
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Error while storing data");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/getBasicInformation", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> processToGetBasicInformation(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);

			PreAccountMaster preAccountMasterMobileData = preAccountMasterService.getPreAccountMasterDataByMobileNumber(preAccountMaster.getStrMobileNo());
			if (preAccountMasterMobileData != null) {
				AccountKycDetails accountKycDetails = accountKycDetailsService.getAccountKycDetails(preAccountMaster.getStrMobileNo());
				if (accountKycDetails == null) {
					preAccountMasterService.saveAccountKycDetails(preAccountMasterMobileData);
				}
				String name = preAccountMasterMobileData.getStrFirstName().concat(preAccountMasterMobileData.getStrMiddleName().concat(preAccountMasterMobileData.getStrLastName()));

				processResponse.setStrName(name);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

				processResponse.setStrDob(formatter.format(preAccountMasterMobileData.getBirthDate()));
				processResponse.setStrGender(preAccountMasterMobileData.getStrGender());
				processResponse.setStrEmail(preAccountMasterMobileData.getStrEmailID());
				processResponse.setStrMobileNumber(preAccountMasterMobileData.getStrMobileNo());
				processResponse.setStrAccountType(preAccountMasterMobileData.getStrAccountType());
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Data retrived successfully");
			} else {
				processResponse.setMessage("Error while retriving the data");
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
			}

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/addressprooftypes", method = RequestMethod.GET)
	public ResponseEntity<?> getAddressTypeInfo(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<String> addressDocumentType = addressProofDocumentTypeService.getAddressDocumentType();

			if (addressDocumentType.size() > 0) 
			{
				List<String> addressType = new ArrayList<String>();
				for (String data : addressDocumentType) 
				{
					addressType.add(data);
				}

				processResponse.setAddressType(addressType);
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Address Type fetched successfully");
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/identityprooftypes", method = RequestMethod.GET)
	public ResponseEntity<?> getIdentityTypeInfo(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			 if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			 
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<String> identityDocumentType = identityProofDocumentTypeService.getIdentityDocumentType();

			if (identityDocumentType.size() > 0) 
			{
				List<String> identityType = new ArrayList<String>();

				for (String data : identityDocumentType) 
				{
					identityType.add(data);
				}

				processResponse.setIdentityType(identityType);
				processResponse.setStatus("S0000");
				processResponse.setResult("Success");
				processResponse.setMessage("Identity type fetched successfully");
			} 
			else 
			{
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// added by ankit on 03-02-2023
	@RequestMapping(value = "/addressInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getAddressDocumentInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountKycDetails accountKycDetails = new ObjectMapper().readValue(decrypt, AccountKycDetails.class);

			boolean isValueExist = accountKycDetailsService.isDocumentValueAlreadyExist(accountKycDetails.getStrIdentityProofDocumentValue());
			if (!isValueExist) 
			{
				String addressDocumentType = addressProofDocumentTypeDao.getPrefixOfDocumentType(accountKycDetails.getStrAddressProofDocumentType());
				accountKycDetails.setStrAddressProofDocumentType(addressDocumentType);
				accountKycDetails.setStrParticipantID(participantId);

				AccountKycDetails existAccountKycDetails = accountKycDetailsService.getAccountKycDetails(accountKycDetails.getStrMobileNo());
				if (existAccountKycDetails != null && existAccountKycDetails.getStrIdentityProofDocumentType() != null) 
				{
					if (!addressDocumentType.equalsIgnoreCase(existAccountKycDetails.getStrIdentityProofDocumentType())) 
					{
						int saveAddressInformation = accountKycDetailsService.saveAddressInformation(accountKycDetails);

						if (saveAddressInformation > 0) {
							processResponse.setStatus("S0000");
							processResponse.setResult("Success");
							processResponse.setMessage("Address Information Saved");
						}
						else 
						{
							processResponse.setStatus("E0000");
							processResponse.setResult("Failure");
							processResponse.setMessage("Address Information cannot Be Saved");
						}
					}
					else
					{
						processResponse.setStatus("E0000");
						processResponse.setResult("Failure");
						processResponse.setMessage("Document Type Already Selected. Please choose Different.");
					}
				}
				else 
				{
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Unable to Fetch Identity Proof already added Document");
				}
			} 
			else 
			{
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Address Document Value Already Added. Please Add Different.");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during POA");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(value = "/identityInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getIdentityDocumentInfo(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountKycDetails accountKycDetails = new ObjectMapper().readValue(decrypt, AccountKycDetails.class);

			boolean isValueExist = accountKycDetailsService.isDocumentValueAlreadyExist(accountKycDetails.getStrIdentityProofDocumentValue());
			if (!isValueExist) 
			{
				String identityDocumentType = identityProofDocumentTypeDao.getPrefixOfDocumentType(accountKycDetails.getStrIdentityProofDocumentType());
				accountKycDetails.setStrIdentityProofDocumentType(identityDocumentType);

				int saveAddressInformation = accountKycDetailsService.saveIdentityInformation(accountKycDetails);
				if (saveAddressInformation > 0) 
				{
					processResponse.setStatus("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("Identity Information Saved");
				}
				else
				{
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Identity Information cannot Be Saved");
				}
			} 
			else
			{
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Identity Document value is already exist. Please add different.");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error during POI");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(path = "/upload/identityDocument", method = RequestMethod.POST)
	public ResponseEntity<?> uploadIdentityDocument(HttpServletRequest request,@RequestParam("image") MultipartFile image,	@RequestParam("mobileNo") String mobileNo) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		// req.setSalt("937ce935f87b2615bbde0d15cdeb37d1");
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			if (image.getSize() < 100_000) {
				long imageUploadFlag = imageService.saveIdentityImage(image, mobileNo);
				if (imageUploadFlag > 0) {
					processResponse.setStatus("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("Image Uploaded Successfully.");
				} else {
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Image Could Not Be Uploaded.");
				}
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// added by ankit on 03-02-2023
	@RequestMapping(path = "/upload/addressDocument", method = RequestMethod.POST)
	public ResponseEntity<?> uploadAddressDocument(HttpServletRequest request,@RequestParam("image") MultipartFile image, @RequestParam("mobileNo") String mobileNo) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			  {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			  }
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			if (image.getSize() < 100_000) {
				long imageUploadFlag = imageService.saveAddressImage(image, mobileNo);
				if (imageUploadFlag > 0) {
					processResponse.setStatus("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("Image Uploaded Successfully");
				} else {
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Image Could Not Be Uploaded");
				}
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// added by prashant Tayde for getting country Code list
	@RequestMapping(path = "/getCountryCodeList", method = RequestMethod.GET)
	public ResponseEntity<?> getCountryCode(HttpServletRequest request) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
            
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<CountryCodeMaster> countryCodeMasters = countryCodeMasterService.getCountryCode(null);

			if (countryCodeMasters != null && countryCodeMasters.size() > 0) {
				processResponse.setCountryCodeMaster(countryCodeMasters);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Country Code fetched successfully");
			} else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}
			// amsLogger.writeInfoLog("SignUpController.getCountryCode()"+countryCodeMasters);
			// return ResponseEntity.ok(countryCodeMasters);

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		// return ResponseEntity.ok("{}");
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// Added By Prashant Tyade for registering account type for customer Start
	@RequestMapping(value = "/registerAccountType", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> getRegisterAccount(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{   
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			} 
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);

			String mobileNo = customerIdService.getMobileNoBasedOnCustId(customerIdCreation);
			if (mobileNo != null) 
			{
				PreSubAccountMaster preSubAccMaster = new PreSubAccountMaster();
				preSubAccMaster.setStrMobileNo(mobileNo);

				String finalAccountType = "";
				if (customerIdCreation.getStrAccountType() != null && customerIdCreation.getStrAccountType().indexOf("-") != -1) 
				{
					finalAccountType = customerIdCreation.getStrAccountType().split("-")[0];
				} 
				else
				{
					finalAccountType = customerIdCreation.getStrAccountType();
				}

				preSubAccMaster.setStrAccountType(finalAccountType);

				//String accountType = preSubAccountMasterService.getAccountTypeExist(preSubAccMaster);
				//added by sunil Y , 2023-07-10  ,adding validation based on account type and cust id [started]
				AccountMaster accountMaster = new AccountMaster();
				accountMaster.setStrAccountType(finalAccountType);
				accountMaster.setStrCustId(customerIdCreation.getStrCustId());
				Boolean isAccountAlreadyCreated = accountMasterService.isAccountAlreadyExistBasedOnType(accountMaster);
				//added by sunil Y , 2023-07-10  ,adding validation based on account type and cust id [end]
				//if (accountType != null)
				if(isAccountAlreadyCreated)
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Please Choose Another Account Type. The Selected Account type is already configured.");
				} 
				else
				{
					//Added by Sunil Yadav for auto and maneual accout creation changes Start
					AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
					accountTypeMaster.setStrAccountType(finalAccountType);
					
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo finalAccountType=["+finalAccountType+"]");
					
					AccountTypeMaster accountTypeObject = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo Application Name=["+CommonConstants.applicationName+"]");
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountTypeObject=["+accountTypeObject+"]");
					if (accountTypeObject != null && accountTypeObject.getStrIsAutomated().equalsIgnoreCase("Y")) 
					{
						customerIdCreation.setStrAccountType(finalAccountType);
						customerIdCreation.setStrParticipantID(participantId);
						processResponse = generateCustomerAccounNo(customerIdCreation, accountTypeObject, processResponse);	
						return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
					}
					//Added by Sunil Yadav for auto and maneual accout creation changes End
					
					preSubAccMaster.setDateOfCreation(new Date());
					preSubAccMaster.setStrIsAccountNoCreated("N");
					preSubAccMaster.setStrIsCustIdCreated("Y");
					preSubAccMaster.setStrCreatedBy("System");

					PreSubAccountMaster preSubAccountMaster = preSubAccountMasterService.savePreSubAccountMaster(preSubAccMaster);

					if (preSubAccountMaster.getStrID() != null) 
					{
						// Added by Sunny soni for regarding mail change Start
						try
						{
							PreAccountMaster preAccountMasterInfo = preAccountMasterService.getPreAccountInfo(mobileNo);
							if (preAccountMasterInfo != null) 
							{
								StringBuilder bodyMsg = new StringBuilder("Dear User, ");
								bodyMsg.append("<br/><br/>");
								bodyMsg.append("There is a request from Customer Mr/Mrs "+ preAccountMasterInfo.getStrFirstName() + " "+ preAccountMasterInfo.getStrLastName() + "");
								bodyMsg.append("<br/>");
								bodyMsg.append("bearing Cust ID " + customerIdCreation.getStrCustId() + " For Account Type " + customerIdCreation.getStrAccountType() + " on " + Utils.simpleDateTimeFormat2.format(new Date()) + "");
								bodyMsg.append("<br/>");
								bodyMsg.append("Kindly log into Back-office and authorise the request to allocate Account Number.");
								// bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
								String subject = "Regarding to Account Generation";
								sendMailToMultiplePerson(bodyMsg.toString(), subject);
							}
						} 
						catch (Exception e) 
						{
							amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
						}
						// Added by Sunny soni for regarding mail change End

						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("Thank you for your request You will assigned an Acoount Number Shortly");
					} 
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Account type Addition failed against Cust id.");
					}
				}
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("User not found of Entered Cust id");
			}

		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	// Added By Prashant Tyade for registering account type for customer End

	private void sendMailToMultiplePerson(String bodyMsg, String subject) {
		try {
			ArrayList<String> toMailId = Utils.getGroupMailId();
			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMailToMultiple(
					"contactus@AMStechnologies.com", toMailId, subject, bodyMsg);
			emailService.sendSimpleHtmlContentMessage(emailTemplate);
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}

	// Added by Ankit For NIGERIA CHANGE Start
	private CustomerIdCreation mapDataToCustomerIdCreation(PreAccountMaster preAccountMasterData, String custId) throws Exception 
	{
		String strAccountType = preAccountMasterData.getStrAccountType();
		String strFirstName = preAccountMasterData.getStrFirstName();
		String strMiddleName = preAccountMasterData.getStrMiddleName();
		String strLastName = preAccountMasterData.getStrLastName();
		String strCountryCode = preAccountMasterData.getStrCountryCode();
		String strMobileNo = preAccountMasterData.getStrMobileNo();
		String strEmailID = preAccountMasterData.getStrEmailID();

		// address could not be get
		Date birthDate = preAccountMasterData.getBirthDate();
		String strGender = preAccountMasterData.getStrGender();
		String strCity = preAccountMasterData.getCity();
		String strAddress = preAccountMasterData.getAddress();
		String strAddress2 = preAccountMasterData.getAddress2();
		String strAddress3 = preAccountMasterData.getAddress3();

		String strState = preAccountMasterData.getState();
		String strPinCode = preAccountMasterData.getPincode();
		String strCountry = preAccountMasterData.getCountry();
		String strTitle = preAccountMasterData.getTitle();

		CustomerIdCreation customerIdCreation = new CustomerIdCreation();
		customerIdCreation.setStrCustId(custId);

		// tier 1 will be set always during registration Start
		customerIdCreation.setStrActiveTier("tier1");
		customerIdCreation.setStrActiveTierDate(Utils.getCurrentDate());
		customerIdCreation.setStrActiveTierTime(Utils.getFormattedCurrentTime());
		customerIdCreation.setStrTier1Date(Utils.getCurrentDate());
		customerIdCreation.setStrTier1Time(Utils.getFormattedCurrentTime());
		customerIdCreation.setStrTier1Status("A");
		// tier 1 will be set always during registration End

		customerIdCreation.setStrAccountType(strAccountType);
		customerIdCreation.setStrCountry(strCountry);
		customerIdCreation.setBirthDate(birthDate);
		customerIdCreation.setStrEmailID(strEmailID);
		customerIdCreation.setStrGender(strGender);
		customerIdCreation.setStrLastName(strLastName);
		customerIdCreation.setStrMiddleName(strMiddleName);
		customerIdCreation.setStrPhoneCode(strCountryCode);
		customerIdCreation.setStrMobileNo(strMobileNo);
		customerIdCreation.setStrState(strState);
		customerIdCreation.setStrTitle(strTitle);
		customerIdCreation.setStrFirstName(strFirstName);
		customerIdCreation.setStrAddress1(strAddress);
		customerIdCreation.setAddress2(strAddress2);
		customerIdCreation.setAddress3(strAddress3);
		customerIdCreation.setStrCity(strCity);
		customerIdCreation.setStrPinCode(strPinCode);

		return customerIdCreation;
	}

	//private ProcessResponse setPasswordWithNigeriaRegistration(PreAccountMaster preAccountMaster) throws Exception {
	private ProcessResponse setPasswordWithNigeriaRegistration(PreAccountMaster preAccountMaster,String participantId) throws Exception {
		ProcessResponse processResponse = new ProcessResponse();

		PreAccountMaster preAccountMasterData = preAccountMasterService.getPreAccountMasterDataByMobileNumber(preAccountMaster.getStrMobileNo());
		if (preAccountMasterData == null) {
			processResponse.setStatus("E0000");
			processResponse.setResult("Failure");
			processResponse.setMessage("User Account Not Found.");
			return processResponse;
		}

		if (preAccountMaster.getStrPassword() != null && preAccountMaster.getStrPassword().trim().length() > 0) {
			String encyptedPassword = encryptDecryptUtil.encrypt(preAccountMaster.getStrPassword());
			preAccountMaster.setStrPassword(encyptedPassword);

			UsersPassword usersPassword = new UsersPassword();
			usersPassword.setStrMobileNo(preAccountMaster.getStrMobileNo());
			usersPassword.setStrNewPassword(encyptedPassword);

			int id = usersPasswordService.addUsersPassword(usersPassword);
			if (id > 0) {
				preAccountMaster.setStrIsAccountNoCreated("N");
				preAccountMaster.setIsCustIdCreated("N"); // Customer id not created flag
				preAccountMaster.setStrAccountCreatedFlag("Y");// After Completing Sign up
				preAccountMaster.setStrIsKycVerified("N");
				preAccountMaster.setStrDateOfCreation(Utils.getCurrentDate()); // New Added By Sunny Soni

				int result = preAccountMasterService.updatePreAccountMasterBasedOnParameter(preAccountMaster);
				if (result > 0) {
					CustomerIdCreation customerIdCreation = customerIDCreationService
							.getCustomerIdInfoByMobile(preAccountMaster.getStrMobileNo());
					if (customerIdCreation != null && customerIdCreation.getStrCustId() != null) {
						processResponse.setStatus("E0000");
						processResponse.setResult("Failure");
						processResponse.setMessage("Customer ID Alreay Created.");
						return processResponse;
					}

					// getting the cust Id
					CustomerIdMap custId = customerIdCreationConfig.getCustId(); // 1131
					String strCustID = custId.getStrCustID();

					// cust id is assigned to Customer_Master of User
					CustomerIdCreation customerIdCreationData = mapDataToCustomerIdCreation(preAccountMasterData, strCustID);
					customerIdCreationData.setStrParticipantID(participantId);
					customerIDCreationService.insertCustId(customerIdCreationData); // Entry in Customer Master Table

					CustomerIdTable custIdTable = new CustomerIdTable();
					custIdTable.setStrAction(custId.getStrAction());
					custIdTable.setstrLastTxnSerialNo(strCustID);
					custIdTable.setstrJulianDate(custId.getStrJulianDate());
					custIdTable.setStrYear(custId.getStrYear());

					customerIdTableService.updateCustIdTable(custIdTable);

					AccountKycDetails accountKycDetails = new AccountKycDetails();
					accountKycDetails.setStrCustId(custId.getStrCustID());
					accountKycDetails.setStrParticipantID(participantId);   //set participantId against registration kyc info---sagark
					accountKycDetails.setStrMobileNo(preAccountMaster.getStrMobileNo());

					//accountKycDetailsService.saveAddressInformation(accountKycDetails);
					accountKycDetailsService.updateKycDetail(accountKycDetails);

					sendRegistrationMailToCustomer(customerIdCreationData, customerIdCreationData.getStrEmailID());

					processResponse.setStatus("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("Dear Customer , Your registration has been completed succesfully."
							+ " Your Customer ID is " + strCustID
							+ ", kindly login to the app using your mobile number as your user ID.");
				} else {
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Password addition failed!");
				}
			} else {
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("User Password addition failed!");
			}
		} else {
			processResponse.setStatus("E0000");
			processResponse.setResult("Failure");
			processResponse.setMessage("Password Can Not Be Blank.");
		}
		return processResponse;
	}

	// added by ankit to send mail to customer on 11-05-2023
	private void sendRegistrationMailToCustomer(CustomerIdCreation customerIdCreation, String emailId) {
		try {
			amsLogger.writeInfoLog("emailId::[" + emailId + "]");
			String subject = "Regarding to Customer ID";

			StringBuilder customerNameSb = new StringBuilder();
			customerNameSb.append(customerIdCreation.getStrFirstName());
			if (customerIdCreation.getStrMiddleName() != null
					&& customerIdCreation.getStrMiddleName().trim().length() > 0) {
				customerNameSb.append(" ").append(customerIdCreation.getStrMiddleName());
			}
			if (customerIdCreation.getStrLastName() != null
					&& customerIdCreation.getStrLastName().trim().length() > 0) {
				customerNameSb.append(" ").append(customerIdCreation.getStrLastName());
			}

			StringBuilder bodyMsg = new StringBuilder("Dear ");
			bodyMsg.append(customerNameSb.toString());
			bodyMsg.append(",");
			bodyMsg.append("<br/><br/>");
			bodyMsg.append("Your Registration has been successfully completed and your Customer ID is ");
			bodyMsg.append(customerIdCreation.getStrCustId());
			bodyMsg.append("<br/>");
			bodyMsg.append("Kindly log into App using Your Mobile no AS UserId.");
			// bodyMsg.append("<br/><br/><br/>");
			// bodyMsg.append("Powered by AMS Technologies Pvt Ltd");

			EmailTemplate emailTemplate = Utils.getEmailTemplateForSendMail("contactus@AMStechnologies.com", emailId,
					subject, bodyMsg.toString());
			emailService.sendSimpleHtmlContentMessage(emailTemplate);
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
	}

	// added by ankit to send mail to customer on 11-05-2023
	// added by ankit on 10-05-2023 -start-
	@RequestMapping(path = "/upload/tier1PassportPhoto", method = RequestMethod.POST)
	public ResponseEntity<?> uploadTier1PassportPhoto(HttpServletRequest request,@RequestParam("image") MultipartFile image, @RequestParam("mobileNo") String mobileNo) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps);
			
			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			 {
				  return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			 }
			
			String participantId = appInfo.getStrParticipantId();
			
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);
			amsLogger.writeInfoLog("image.getSize()::"+image.getSize());
			if (image.getSize() < 100_000) 
			{
				long imageUploadFlag = imageService.saveTier1PassportPhoto(image, mobileNo);  //,participantId,participantId
				amsLogger.writeInfoLog("imageUploadFlag::"+imageUploadFlag);
				if (imageUploadFlag > 0) 
				{
					processResponse.setStatus("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("Image Uploaded Successfully");
				}
				else 
				{
					processResponse.setStatus("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Image Could Not Be Uploaded");
				}
			} 
			else
			{
				processResponse.setStatus("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setStatus("Failed");
			processResponse.setCode("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	@RequestMapping(path = "/upload/tier2PassportPhoto", method = RequestMethod.POST)
	public ResponseEntity<?> uploadTier2PassportPhoto(HttpServletRequest request,@RequestParam("image") MultipartFile image, @RequestParam("mobileNo") String mobileNo) throws FileNotFoundException 
	{
	    ProcessResponse processResponse = new ProcessResponse();
	    PayloadReqRes req = new PayloadReqRes();
	    try 
	    {
	        String salt = jwtUtil.getRandomSalt();
	        req.setSalt(salt);
	        amsLogger.writeInfoLog("image.getSize()::" + image.getSize());
	        if (image.getSize() < 100_000) 
	        {
	            long imageUploadFlag = imageService.saveTier2PassportPhoto(image, mobileNo); //,participantId,participantId
	            amsLogger.writeInfoLog("imageUploadFlag::" + imageUploadFlag);
	            if (imageUploadFlag > 0) {
	                processResponse.setStatus("S0000");
	                processResponse.setResult("Success");
	                processResponse.setMessage("Image Uploaded Successfully");
	            } else {
	                processResponse.setStatus("E0000");
	                processResponse.setResult("Failure");
	                processResponse.setMessage("Image Could Not Be Uploaded");
	            }
	        } else {
	            processResponse.setStatus("E0000");
	            processResponse.setResult("Failure");
	            processResponse.setMessage("Allowed size of image is 100kb.");
	        }
	    } catch (Exception e) {
	        amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
	        processResponse.setCode("E0000");
	        processResponse.setStatus("Failed");
	        processResponse.setMessage("Internal Server Error - " + e.getMessage());
	    }
        return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	// Added By Sunil Yadav for registering account with Auto Creation for customer Start
	@RequestMapping(value = "/autoAccountGeneration", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> autoAccountCreation(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setStatus("Success");
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);

			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(customerIdCreation.getStrAccountType());
			
			AccountTypeMaster accountTypeObject = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if (accountTypeObject != null) 
			{
				processResponse = generateCustomerAccounNo(customerIdCreation, accountTypeObject, processResponse);				
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account type Not Found");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	private ProcessResponse generateCustomerAccounNo(CustomerIdCreation customerIdCreation, AccountTypeMaster accountTypeObject, ProcessResponse processResponse) 
	{
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			List<CustomerIdCreation> customerAccountDetailsBasedOnCustId = customerIDCreationService.getCustomerAccountDetailsBasedOnCustId(customerIdCreation);
			if (customerAccountDetailsBasedOnCustId.size() > 0) 
			{
				processResponse = generateCustomerAccounNo(accountTypeObject, processResponse);
				if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
				{
					CustomerIdCreation customerIdCreationMaster = customerAccountDetailsBasedOnCustId.get(0);
					
					AccountCreation accountCreationInstance = new AccountCreation();
					accountCreationInstance.setStrParticipantID(customerIdCreation.getStrParticipantID());					
					accountCreationInstance.setStrAccountNumber(processResponse.getAccountNumber());
					
					customerIdCreationMaster.setStrAccountType(customerIdCreation.getStrAccountType());
					customerIdCreationMaster.setStrParticipantID(customerIdCreation.getStrParticipantID());
					
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo customerIdCreationMaster::"+customerIdCreationMaster);
					createAccountMasterData(customerIdCreationMaster, accountCreationInstance);				
					
					if (!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
					{
						setAccountLimitsInfo(accountCreationInstance);
					}
					
					if (accountTypeObject.getStrAccountTypeCategory().equalsIgnoreCase("C")) 
					{
						setDefaultAccountCreditLimitsInfo(accountCreationInstance);
					}

					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountCreationInstance--222---::"+accountCreationInstance);
					accountCreationInstance = accountMasterService.saveAutoCreationInfo(accountCreationInstance, accountTypeObject);
					
					
					if("Y".equalsIgnoreCase(accountTypeObject.getIsMultiCurrencySupport().toUpperCase()))
					{
						int multicurrencyWallet = accountMasterService.createSubAccountForMultiCurrency(accountCreationInstance);
						System.out.println("multicurrencyWallet"+multicurrencyWallet);
						
						acTypeLrsTcsMasterService.createMultiCurrencyFinancialYearMasterForAccount(accountCreationInstance);	
						
					}
										
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountCreationInstance--*****888---::"+accountCreationInstance);
					if (accountCreationInstance!=null && accountCreationInstance.getStrID()!=null) 
					{
						/* Changes done for QrCode Genaration and update in account_master - Start */
						QrInfo qrInfo = new QrInfo();
						qrInfo.setAccount_no(accountCreationInstance.getStrAccountNumber());
						qrInfo.setMobile_no(accountCreationInstance.getStrMobileNo());
						qrInfo.setAccount_name(accountCreationInstance.getStrFirstName());
						qrInfo.setAccount_type(accountCreationInstance.getStrAccountType());
						
						qrCodeService.updateQrCodetoAccount(qrInfo);
						/* Changes done for QrCode Genaration and update in account_master - End */
						
						processResponse.setStrMobileNumber(accountCreationInstance.getStrMobileNo());
						processResponse.setStrEmail(accountCreationInstance.getStrEmailID());
						
						sendMailToCustomer(processResponse.getAccountNumber(), processResponse.getStrEmail(), processResponse.getStrMobileNumber());
						
						processResponse.setMessage("Successfully account number generated.Please check on your Email.");
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Issue Occurs during Account saving!!");
					}
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Customer Not Found");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse generateCustomerAccounNo(AccountTypeMaster accountTypeObject, ProcessResponse processResponse) 
	{
		try 
		{
			amsLogger.writeInfoLog("Inside generateCustomerAccounNo Application Name=["+CommonConstants.applicationName+"]");
			if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				// generating 10 digit nuban account no
				String strNubanType = accountTypeObject.getStrNubanType();
				
				NUBANAccountDto nubanAccountDto = new NUBANAccountDto();
				nubanAccountDto.setStrNubanType(strNubanType);
				nubanAccountDto.setStrAccountType(accountTypeObject.getStrAccountType());
				amsLogger.writeInfoLog("nubanAccountDto::"+nubanAccountDto);
				
				NUBANAccountDto accountNo = nubanCodeConfigService.getAccountNo(nubanAccountDto);
				amsLogger.writeInfoLog("accountNo::["+accountNo+"]");
				
				String strAccountNo = accountNo.getStrAccountNo();
				String finalAccountNo = strAccountNo.substring(6);
				processResponse.setAccountNumber(finalAccountNo);
			} 
			else 
			{
				String finalAccountNo = Utils.getUpdatedAccNumber(accountTypeObject.getStrLastAccNumber());
				processResponse.setAccountNumber(finalAccountNo);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Server Issue occurs during account number generation");
		}
		amsLogger.writeInfoLog("Inside generateCustomerAccounNo ***processResponse***=["+processResponse+"]");
		return processResponse;
	}
	
	private AccountCreation createAccountMasterData(CustomerIdCreation customerIdCreationMaster, AccountCreation accountCreationInstance) 
	{
		try 
		{
			accountCreationInstance.setStrCustId(customerIdCreationMaster.getStrCustId());
			accountCreationInstance.setStrAccountType(customerIdCreationMaster.getStrAccountType());
			
			accountCreationInstance.setStrFirstName(customerIdCreationMaster.getStrFirstName());
			accountCreationInstance.setStrLastName(customerIdCreationMaster.getStrLastName());
			accountCreationInstance.setStrMiddleName(customerIdCreationMaster.getStrMiddleName());
			accountCreationInstance.setStrCity(customerIdCreationMaster.getStrCity());
			accountCreationInstance.setStrCountry(customerIdCreationMaster.getStrCountry());
			accountCreationInstance.setStrGender(customerIdCreationMaster.getStrGender());
			accountCreationInstance.setStrMobileNo(customerIdCreationMaster.getStrMobileNo());
			accountCreationInstance.setStrTitle(customerIdCreationMaster.getStrTitle());
			accountCreationInstance.setStrPhoneCode(customerIdCreationMaster.getStrPhoneCode());
			accountCreationInstance.setStrPinCode(customerIdCreationMaster.getStrPinCode());
			accountCreationInstance.setStrState(customerIdCreationMaster.getStrState());
			accountCreationInstance.setBirthDate(customerIdCreationMaster.getBirthDate());
			accountCreationInstance.setStrAddress1(customerIdCreationMaster.getStrAddress1());
			accountCreationInstance.setStrAddress2(customerIdCreationMaster.getAddress2());
			accountCreationInstance.setStrAddress3(customerIdCreationMaster.getAddress3());
			accountCreationInstance.setStrEmailID(customerIdCreationMaster.getStrEmailID());
			accountCreationInstance.setStrDOB(customerIdCreationMaster.getStrDOB());
			accountCreationInstance.setStrParticipantID(customerIdCreationMaster.getStrParticipantID());
			
			accountCreationInstance.setStrDateOfCreation(Utils.getCurrentDate());
			
			accountCreationInstance.setStrCreatedBy("System");
			accountCreationInstance.setStrStatus("Active");
			accountCreationInstance.setStrClosingBalance("0");
			accountCreationInstance.setStrOpeningBalance("0");
			accountCreationInstance.setStrLoadCount("0");
			accountCreationInstance.setStrIsLinkedwithCard("N");
			accountCreationInstance.setStrIsInstantAccount("N");
			accountCreationInstance.setStrTotalOutstandingBal("0");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("Inside createAccountMasterData accountCreationInstance::"+accountCreationInstance);
		return accountCreationInstance;
	}
	private AccountCreation setAccountLimitsInfo(AccountCreation accountCreationInstance) 
	{
		try 
		{
			AccountTransactionLimitation accountTransactionLimitation = new AccountTransactionLimitation();
			accountTransactionLimitation.setStrAccountType(accountCreationInstance.getStrAccountType());
			
			AccountTransactionLimitation accountTransactionLimitationObj = accountTransactionLimitationService.getAccountTxnLimitBasedOnParam(accountTransactionLimitation);
			if (accountTransactionLimitationObj != null) 
			{
				accountCreationInstance.setStrAvailableDailyLimit(accountTransactionLimitationObj.getStrDailyTxnLimit());
				accountCreationInstance.setStrAvailableMonthlyLimit(accountTransactionLimitationObj.getStrMonthlyTxnLimit());
				accountCreationInstance.setStrAvailableYearlyLimit(accountTransactionLimitationObj.getStrYearlyTxnLimit());
				
				accountCreationInstance.setStrDailyTxnLimit(Integer.valueOf(accountTransactionLimitationObj.getStrDailyTxnLimit()));
				accountCreationInstance.setStrMonthlyTxnLimit(accountTransactionLimitationObj.getStrMonthlyTxnLimit());
				accountCreationInstance.setStrYearlyTxnLimit(accountTransactionLimitationObj.getStrYearlyTxnLimit());
				accountCreationInstance.setStrSingleTxnLimit(Integer.valueOf(accountTransactionLimitationObj.getStrSingleTxnLimit()));
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreationInstance;
	}
	private AccountCreation setDefaultAccountCreditLimitsInfo(AccountCreation accountCreationInstance) 
	{
		try 
		{
			AccountCreditLimitCategory accountCreditLimitCategory = new AccountCreditLimitCategory();
			accountCreditLimitCategory.setStrParticipantId(accountCreationInstance.getStrParticipantID());
			accountCreditLimitCategory.setStrCreditType("GENERAL");
			
			accountCreditLimitCategory = accountCreditLimitCategoryDao.getAccountCreditLimitCategoryObj(accountCreditLimitCategory);
			if (accountCreditLimitCategory != null) 
			{
				accountCreationInstance.setStrCreditLimitAmount(accountCreditLimitCategory.getStrCreditLimit());
				accountCreationInstance.setStrCreditLimitCategory(accountCreditLimitCategory.getStrCreditType());
				accountCreationInstance.setStrAvailableCreditLimit(accountCreditLimitCategory.getStrCreditLimit());
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountCreationInstance;
	}
	
	private boolean sendMailToCustomer(String autoGeneratedAcNo, String custEmailId, String custMobileNo) 
	{
		try 
		{
			String subject = "Account Creation Details";
			StringBuilder bodyMsg = new StringBuilder("Dear User,<br/><br/>");
			bodyMsg.append("Please find the below account number and user id : <br/> Account Number is : <b>");
			bodyMsg.append(autoGeneratedAcNo);
			bodyMsg.append(" </b> <br/> User Id is: <b>");
			bodyMsg.append(custMobileNo);
			bodyMsg.append("</b><br/><br/><br/>");
			bodyMsg.append("Powered by AMS Technologies Pvt Ltd.");

			EmailTemplate emailTemplate = new EmailTemplate();
			emailTemplate.setStrFrom("contactus@AMStechnologies.com");
			emailTemplate.setStrTo(custEmailId);
			emailTemplate.setStrSubject(subject);
			emailTemplate.setStrText(bodyMsg.toString());
			
			String result =	emailService.sendSimpleHtmlContentMessage(emailTemplate);
			if (result!=null) 
			{
				return true;
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
	
	//Add Additional Account Start
	
	@RequestMapping(value = "/addAccount", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> addAccount(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CustomerIdCreation customerIdCreation = new ObjectMapper().readValue(decrypt, CustomerIdCreation.class);
			
			//montraId and CustId validation
			String mobileNo = customerIdService.getMobileNoBasedOnCustId(customerIdCreation);
			if (mobileNo != null) 
			{
				PreSubAccountMaster preSubAccMaster = new PreSubAccountMaster();
				preSubAccMaster.setStrMobileNo(mobileNo);

				String finalAccountType = "";
				if (customerIdCreation.getStrAccountType() != null && customerIdCreation.getStrAccountType().indexOf("-") != -1) 
				{
					finalAccountType = customerIdCreation.getStrAccountType().split("-")[0];
				} 
				else
				{
					finalAccountType = customerIdCreation.getStrAccountType();
				}

				preSubAccMaster.setStrAccountType(finalAccountType);

				//String accountType = preSubAccountMasterService.getAccountTypeExist(preSubAccMaster);
				//added by sunil Y , 2023-07-10  ,adding validation based on account type and cust id [started]
				AccountMaster accountMaster = new AccountMaster();
				accountMaster.setStrAccountType(finalAccountType);
				accountMaster.setStrCustId(customerIdCreation.getStrCustId());
				Boolean isAccountAlreadyCreated = accountMasterService.isAccountAlreadyExistBasedOnType(accountMaster);
				//added by sunil Y , 2023-07-10  ,adding validation based on account type and cust id [end]
				//if (accountType != null)
				if(isAccountAlreadyCreated)
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Please Choose Another Account Type. The Selected Account type is already configured.");
				} 
				else
				{
					//Added by Sunil Yadav for auto and maneual accout creation changes Start
					AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
					accountTypeMaster.setStrAccountType(finalAccountType);
					
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo finalAccountType=["+finalAccountType+"]");
					
					AccountTypeMaster accountTypeObject = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo Application Name=["+CommonConstants.applicationName+"]");
					amsLogger.writeInfoLog("Inside generateCustomerAccounNo accountTypeObject=["+accountTypeObject+"]");
					if (accountTypeObject != null && accountTypeObject.getStrIsAutomated().equalsIgnoreCase("Y")) 
					{
						customerIdCreation.setStrAccountType(finalAccountType);
						processResponse = generateCustomerAccounNo(customerIdCreation, accountTypeObject, processResponse);	
						return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
					}
					//Added by Sunil Yadav for auto and maneual accout creation changes End
					
					preSubAccMaster.setDateOfCreation(new Date());
					preSubAccMaster.setStrIsAccountNoCreated("N");
					preSubAccMaster.setStrIsCustIdCreated("Y");
					preSubAccMaster.setStrCreatedBy("System");

					PreSubAccountMaster preSubAccountMaster = preSubAccountMasterService.savePreSubAccountMaster(preSubAccMaster);

					if (preSubAccountMaster.getStrID() != null) 
					{
						// Added by Sunny soni for regarding mail change Start
						try
						{
							PreAccountMaster preAccountMasterInfo = preAccountMasterService.getPreAccountInfo(mobileNo);
							if (preAccountMasterInfo != null) 
							{
								StringBuilder bodyMsg = new StringBuilder("Dear User, ");
								bodyMsg.append("<br/><br/>");
								bodyMsg.append("There is a request from Customer Mr/Mrs "+ preAccountMasterInfo.getStrFirstName() + " "+ preAccountMasterInfo.getStrLastName() + "");
								bodyMsg.append("<br/>");
								bodyMsg.append("bearing Cust ID " + customerIdCreation.getStrCustId() + " For Account Type " + customerIdCreation.getStrAccountType() + " on " + Utils.simpleDateTimeFormat2.format(new Date()) + "");
								bodyMsg.append("<br/>");
								bodyMsg.append("Kindly log into Back-office and authorise the request to allocate Account Number.");
								// bodyMsg.append("Powered by AMS Technologies Pvt Ltd");
								String subject = "Regarding to Account Generation";
								sendMailToMultiplePerson(bodyMsg.toString(), subject);
							}
						} 
						catch (Exception e) 
						{
							amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
						}
						// Added by Sunny soni for regarding mail change End

						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("Thank you for your request You will assigned an Acoount Number Shortly");
					} 
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Account type Addition failed against Cust id.");
					}
				}
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("User not found of Entered Cust id");
			}

		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//Add Additional Account End
	
	//POI And POA related changes Start
	@RequestMapping(path = "/upload/poi", method = RequestMethod.POST)//Note:Need to add changes for montra Id and CustId 
	public ResponseEntity<?> uploadIProofOfdentityDocument(@RequestParam("image") MultipartFile image, @RequestParam("montraId") String montraId, @RequestParam("strCustId") String strCustId) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		// req.setSalt("937ce935f87b2615bbde0d15cdeb37d1");
		try {
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			if (image.getSize() < 100_000) 
			{
				long imageUploadFlag = imageService.saveIdentityImage(image, strCustId);
				if (imageUploadFlag > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("POI Uploaded Successfully.");
				} else {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failure");
					processResponse.setMessage("POI Could Not Be Uploaded.");
				}
			} else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("Failed");
			processResponse.setStatus("E0000");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	// added by ankit on 03-02-2023
	@RequestMapping(path = "/upload/poa", method = RequestMethod.POST)//Note:Need to add changes for montra Id and CustId 
	public ResponseEntity<?> uploadProofOfAddressDocument(@RequestParam("image") MultipartFile image, @RequestParam("montraId") String montraId, @RequestParam("strCustId") String strCustId) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try
		{
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			if (image.getSize() < 100_000) 
			{
				long imageUploadFlag = imageService.saveAddressImage(image, strCustId);
				if (imageUploadFlag > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("POA Uploaded Successfully");
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failure");
					processResponse.setMessage("POA Could Not Be Uploaded");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/addPOAInfo", method = RequestMethod.POST) //[Note: need to add changes for montraId and custId validation and making entry]
	public ResponseEntity<?> addPOADocumentInfo(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountKycDetails accountKycDetails = new ObjectMapper().readValue(decrypt, AccountKycDetails.class);

			boolean isValueExist = accountKycDetailsService.isDocumentValueAlreadyExist(accountKycDetails.getStrIdentityProofDocumentValue());
			if (!isValueExist) 
			{
				String addressDocumentType = addressProofDocumentTypeDao.getPrefixOfDocumentType(accountKycDetails.getStrAddressProofDocumentType());
				accountKycDetails.setStrAddressProofDocumentType(addressDocumentType);

				AccountKycDetails existAccountKycDetails = accountKycDetailsService.getAccountKycDetails(accountKycDetails.getStrMobileNo());
				if (existAccountKycDetails != null && existAccountKycDetails.getStrIdentityProofDocumentType() != null) 
				{
					if (!addressDocumentType.equalsIgnoreCase(existAccountKycDetails.getStrIdentityProofDocumentType())) 
					{
						int saveAddressInformation = accountKycDetailsService.saveAddressInformation(accountKycDetails);

						if (saveAddressInformation > 0) {
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
							processResponse.setMessage("POA Information Saved");
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failure");
							processResponse.setMessage("POA Information cannot Be Saved");
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failure");
						processResponse.setMessage("Document Type Already Selected. Please choose Different.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failure");
					processResponse.setMessage("Unable to Fetch Identity Proof already added Document");
				}
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Address Document Value Already Added. Please Add Different.");
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error during POA");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/addPOIInfo", method = RequestMethod.POST)//[Note: need to add changes for montraId and custId validation and making entry]
	public ResponseEntity<?> addPOIDocumentInfo(@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountKycDetails accountKycDetails = new ObjectMapper().readValue(decrypt, AccountKycDetails.class);

			boolean isValueExist = accountKycDetailsService.isDocumentValueAlreadyExist(accountKycDetails.getStrIdentityProofDocumentValue());
			if (!isValueExist) 
			{
				String identityDocumentType = identityProofDocumentTypeDao.getPrefixOfDocumentType(accountKycDetails.getStrIdentityProofDocumentType());
				accountKycDetails.setStrIdentityProofDocumentType(identityDocumentType);

				int saveAddressInformation = accountKycDetailsService.saveIdentityInformation(accountKycDetails);
				if (saveAddressInformation > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setResult("Success");
					processResponse.setMessage("POI Information Saved");
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setResult("Failure");
					processResponse.setMessage("Identity Information cannot Be Saved");
				}
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setResult("Failure");
				processResponse.setMessage("Identity Document value is already exist. Please add different.");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setResult("Failed");
			processResponse.setMessage("Internal Server Error during POI");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value = "/poiTypes", method = RequestMethod.GET)
	public ResponseEntity<?> getPOITypes() 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<String> identityDocumentType = identityProofDocumentTypeService.getIdentityDocumentType();

			if (identityDocumentType.size() > 0) 
			{
				List<String> identityType = new ArrayList<String>();

				for (String data : identityDocumentType) 
				{
					identityType.add(data);
				}

				processResponse.setIdentityType(identityType);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Identity type fetched successfully");
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	@RequestMapping(value = "/poaTypes", method = RequestMethod.GET)
	public ResponseEntity<?> getPOAtypes() 
	{
		ProcessResponse processResponse = new ProcessResponse();
		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);

			List<String> addressDocumentType = addressProofDocumentTypeService.getAddressDocumentType();

			if (addressDocumentType.size() > 0) 
			{
				List<String> addressType = new ArrayList<String>();
				for (String data : addressDocumentType) 
				{
					addressType.add(data);
				}

				processResponse.setAddressType(addressType);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Address Type fetched successfully");
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//POI And POA related changes End
	
	//Added tier related changes Start
	@RequestMapping(path = "/upload/tier1Photo", method = RequestMethod.POST)//[Note: need to add changes for montraId and custId validation and making entry]
	public ResponseEntity<?> uploadTier1Photo(@RequestParam("image") MultipartFile image, @RequestParam("montraId") String montraId, @RequestParam("strCustId") String strCustId) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		try 
		{
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);
			if (image.getSize() < 100_000) 
			{
				long imageUploadFlag = imageService.saveTier1PassportPhoto(image, strCustId);
				if (imageUploadFlag > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Tier1 photo Uploaded Successfully");
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failure");
					processResponse.setMessage("Tier1 photo Could Not Be Uploaded");
				}
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb.");
			}
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	@RequestMapping(path = "/upload/tier2Photo", method = RequestMethod.POST)//[Note: need to add changes for montraId and custId validation and making entry]
	public ResponseEntity<?> uploadTier2Photo(@RequestParam("image") MultipartFile image, @RequestParam("cid") String montraId, @RequestParam("strCustId") String strCustId) throws FileNotFoundException 
	{
		ProcessResponse processResponse = new ProcessResponse();

		PayloadReqRes req = new PayloadReqRes();
		try {
			String salt = jwtUtil.getRandomSalt();
			req.setSalt(salt);
			if (image.getSize() < 100_000) 
			{
				long imageUploadFlag = imageService.saveTier2PassportPhoto(image, strCustId);
				if (imageUploadFlag > 0) 
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Tier2 photo Uploaded Successfully.");
				} 
				else {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failure");
					processResponse.setMessage("Tier2 photo Could Not Be Uploaded");
				}
			}
			else {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Allowed Size of Image is 100kb");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::" + ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//Added tier related changes End
}
