package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.ParticipantWiseWalletMaster;
import ams.cms.services.ParticipantWiseWalletService;

@RestController
@RequestMapping("/participant_wallet")
public class ParticipantWiseWalletController 
{
	@Autowired
	ParticipantWiseWalletService participantWiseWalletService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantWiseWallet(@RequestBody ParticipantWiseWalletMaster participantWiseWalletMaster)
	{
		String result = null;
		try 
		{
			participantWiseWalletMaster = participantWiseWalletService.addParticipantWiseWallet(participantWiseWalletMaster);
			if (participantWiseWalletMaster.getStrID()!=null)
			{
				return ResponseEntity.ok(participantWiseWalletMaster);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getParticipantBasedMcc", method = RequestMethod.POST)
	public ResponseEntity<?> getParticipantBasedMccListData(@RequestBody ParticipantWiseWalletMaster participantWiseWalletMaster)
	{
		String result = null;
		try 
		{
			//List<ParticipantWiseWalletMaster> mccListData = participantWiseWalletService.getParticipantBasedMcc(participantWiseWalletMaster);
			List<ParticipantWiseWalletMaster> mccListData = participantWiseWalletService.getParticipantBasedMccAndDescr(participantWiseWalletMaster);
			if (mccListData!=null && mccListData.size() > 0) 
			{
				return ResponseEntity.ok(mccListData);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
}
