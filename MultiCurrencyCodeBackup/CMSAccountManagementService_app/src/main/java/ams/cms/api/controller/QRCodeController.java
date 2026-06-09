/*Added by pankaj Pawar for QR CODE Generation*/
package ams.cms.api.controller;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.QrInfo;
import ams.cms.api.service.QrCodeService;
import ams.cms.logger.AMSLogger;
import ams.cms.services.AccountMasterService;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(QRCodeController.class);
	
	@Autowired
	QrCodeService qrCodeService;
	
	@Autowired
	AccountMasterService accountMasterService;
	
	@Autowired
	ObjectMapper mapper;
	
	@RequestMapping(value = "/genarate", method = RequestMethod.POST)
	public ResponseEntity<?> genarateQrCode(@RequestBody Map<String, Object> mapData)
	{
		try 
		{
			QrInfo qrInfo = new QrInfo();
			qrInfo.setAccount_no(mapData.get("account_no").toString());
			qrInfo.setMobile_no(mapData.get("mobile_no").toString());
			qrInfo.setAccount_name(mapData.get("account_name").toString());
			qrInfo.setAccount_type(mapData.get("account_type").toString());
			
	        int update = qrCodeService.updateQrCodetoAccount(qrInfo);
	        return ResponseEntity.ok(update);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(null);
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code [Start]
	@RequestMapping(value = "/genarateDynamicQr", method = RequestMethod.POST)
	public ResponseEntity<?> genarateDyanmicQrCode(@RequestBody Map<String, Object> mapData)
	{
		try 
		{
			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(mapData.get("account_no").toString());
			accountMaster.setStrCustId(mapData.get("cust_id").toString());
			
			AccountMaster accountMasterResponse = accountMasterService.getAccountQRDetails(accountMaster);
			
			DynamicQrGeneration dynamicqrInfo = new DynamicQrGeneration();
			
			dynamicqrInfo.setFromAccountNumber(mapData.get("account_no").toString());
			dynamicqrInfo.setFromAccountType(accountMasterResponse.getStrAccountType());
			dynamicqrInfo.setAmount(mapData.get("amount").toString());
			dynamicqrInfo.setRefNo(Utils.getRefNo());
			dynamicqrInfo.setQrDate(Utils.getCurrentDate());
			dynamicqrInfo.setQrData(accountMasterResponse.getStrQrCodeData());
			dynamicqrInfo.setAccountMaster(accountMasterResponse);
			dynamicqrInfo.setQrTime(Utils.getFormattedCurrentTime());
			
			dynamicqrInfo = qrCodeService.getDynamicQrCodeImageUrl(dynamicqrInfo);
			if(dynamicqrInfo != null && dynamicqrInfo.getStrQrCodeImageUrl() != null && dynamicqrInfo.getStrQrCodeImageUrl().trim().length() > 0)
			{
				 return ResponseEntity.ok(dynamicqrInfo.getStrQrCodeImageUrl());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok("Image not found");
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code [End]
}
