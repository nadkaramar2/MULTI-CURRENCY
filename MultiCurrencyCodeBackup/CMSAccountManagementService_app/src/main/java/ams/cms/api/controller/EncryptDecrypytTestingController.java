package ams.cms.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.txn.handler.ExternalServerHandler;
import ams.cms.util.AesUtil;
import ams.cms.utility.PayloadReqRes;

@RestController
public class EncryptDecrypytTestingController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(EncryptDecrypytTestingController.class);
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	ExternalServerHandler externalServerHandler;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/encryptedData", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> getEncryptedData(@RequestBody Map<Object, Object> signInRequestParam, HttpServletRequest request)
	{
		String token = request.getHeader("Authorization");
		
		amsLogger.writeInfoLog("Token::"+token);		
		Claims claim = jwtUtil.getClaimsFromToken(token);
		
		String salt = AesUtil.random(16);
		AesUtil aesUtil = new AesUtil(256, 1000);
		
		String payload = aesUtil.encrypt(salt, jwtUtil.getIVPhraseFromClaim(claim), jwtUtil.getPhraseFromClaim(claim), convertJsonToString(signInRequestParam));
		amsLogger.writeInfoLog("payload::"+payload);
		
		PayloadReqRes payloadReqRes = new PayloadReqRes();
		payloadReqRes.setSalt(salt);
		payloadReqRes.setPayload(payload);
		
		amsLogger.writeInfoLog("payloadReqRes::"+payloadReqRes);
		return ResponseEntity.ok(payloadReqRes);
	}
	
	@RequestMapping(value = "/deccryptedData", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> deccryptedData(@RequestBody PayloadReqRes req)
	{
		String decrypt = "";
		try
		{
			amsLogger.writeInfoLog("req::"+req);
			decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			amsLogger.writeInfoLog("decrypt::"+decrypt);
			return ResponseEntity.ok(decrypt);
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			//e.printStackTrace();
		}	
		return ResponseEntity.ok(decrypt);
	}
	
	

	//Added By Sunny Soni Start
	@RequestMapping(value = "/saveBankDetails", method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<?> saveBankDetails(@RequestBody PayloadReqRes req)
	{
		String decrypt = "";
		try
		{
			externalServerHandler.addBankDetailsFromNIBSS("");
			return ResponseEntity.ok("OK");
		} 
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			//e.printStackTrace();
		}	
		return ResponseEntity.ok(decrypt);
	}
	//Added By Sunny Soni End
	
	public String convertJsonToString(Object obj)
	{
		String reqres = "";
		try 
		{
			reqres = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			amsLogger.writeInfoLog("reqres::"+reqres);
		} 
		catch (JsonProcessingException e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			//e.printStackTrace();
		}

		return reqres;
	}
}
