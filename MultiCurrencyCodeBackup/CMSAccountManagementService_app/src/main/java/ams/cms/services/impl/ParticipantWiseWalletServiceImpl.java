package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ParticipantWiseWalletDao;
import ams.cms.model.ParticipantWiseWalletMaster;
import ams.cms.services.ParticipantWiseWalletService;

@Transactional
@Service
public class ParticipantWiseWalletServiceImpl implements ParticipantWiseWalletService
{
	@Autowired
	ParticipantWiseWalletDao participantWiseWalletDao;
	
	@Override
	public List<ParticipantWiseWalletMaster> getParticipantBasedMcc(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception {
		return participantWiseWalletDao.getParticipantBasedMcc(participantWiseWalletMaster);
	}
	
	@Override
	public List<ParticipantWiseWalletMaster> getParticipantBasedMccAndDescr(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception 
	{
		return participantWiseWalletDao.getParticipantBasedMccAndDescr(participantWiseWalletMaster);
	}
	
	@Override
	public ParticipantWiseWalletMaster addParticipantWiseWallet(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception
	{
		try 
		{
			String mccCodecStr = participantWiseWalletMaster.getStrMccCode();
			String participantId = participantWiseWalletMaster.getStrParticipantID();
			String loginUser = participantWiseWalletMaster.getStrCreatedBy();
			
			String[] mccCodeArr = {};
			if (mccCodecStr!=null && mccCodecStr.indexOf(",")!=-1)
			{
				mccCodeArr = mccCodecStr.split(",");
			}
			System.out.println("mccCodeArr::"+mccCodeArr);
			if (mccCodeArr.length > 0)
			{
				List<ParticipantWiseWalletMaster> listOfParticipantWiseWalletMasters = new ArrayList<ParticipantWiseWalletMaster>();
				for (int i = 0; i < mccCodeArr.length; i++)
				{
					String mccCode = mccCodeArr[i];
					if (mccCode!=null && mccCode.trim().length() > 0)
					{
						ParticipantWiseWalletMaster participantWiseWallet = new ParticipantWiseWalletMaster();
						participantWiseWallet.setStrMccCode(mccCodeArr[i]);
						participantWiseWallet.setStrParticipantID(participantId);
						participantWiseWallet.setStrCreatedBy(loginUser);
						//participantWiseWallet.setStrDateOfCreation(new Date());
						
						listOfParticipantWiseWalletMasters.add(participantWiseWallet);
					}
				}
				if (listOfParticipantWiseWalletMasters.size() > 0) 
				{
					int[] resultArr = participantWiseWalletDao.batchEntryforParticipantWiseWallet(listOfParticipantWiseWalletMasters);
					if (resultArr.length > 0)
					{
						participantWiseWalletMaster.setStrID(String.valueOf(resultArr));
					}
				}
			}
			else
			{
				participantWiseWalletMaster.setStrDateOfCreation(new Date());
				participantWiseWalletDao.save(participantWiseWalletMaster);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return participantWiseWalletMaster;
	}
	
}
