package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.ParticipantWiseWalletMaster;
import ams.cms.model.WalletAccountMaster;
import ams.cms.services.WalletAccountMasterService;

@RestController
@RequestMapping("/wallet_account")
public class WalletAccountMasterController 
{
	@Autowired
	WalletAccountMasterService walletAccountMasterService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantWiseWallet(@RequestBody ParticipantWiseWalletMaster participantWiseWalletMaster)
	{
		String result = null;
		try 
		{
			
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/getLinkedAccountWalletList", method = RequestMethod.POST)
	public ResponseEntity<?> getLinkedAccountWalletList(@RequestBody WalletAccountMaster walletAccountMaster)
	{
		String result = null;
		try 
		{
			List<WalletAccountMaster> walletAccountMasters = walletAccountMasterService.getLinkedAccountWalletList(walletAccountMaster);
			return ResponseEntity.ok(walletAccountMasters);
		}
		catch (Exception e) {
			System.out.println("Exception in getLinkedAccountWalletList::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
