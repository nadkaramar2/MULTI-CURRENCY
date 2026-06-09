package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.WalletBalanceResponse;
import ams.cms.api.model.WalletBalanceShowResponse;
import ams.cms.dao.WalletAccountMasterDao;
import ams.cms.model.WalletAccountMaster;
import ams.cms.services.WalletAccountMasterService;
import ams.cms.util.ProcessResponse;

@Transactional
@Service
public class WalletAccountMasterServiceImpl implements WalletAccountMasterService
{
	@Autowired
	WalletAccountMasterDao walletAccountMasterDao;
	
	@Override
	public int createWalletAccountBasedOnMccWise(List<WalletAccountMaster> walletAccountMasters)
	{
		int response = 0;
		try 
		{
			int[] responseArr = walletAccountMasterDao.batchEntryOfWalletAccountMaster(walletAccountMasters);
			if (responseArr != null && responseArr.length > 0)
			{
				response = responseArr.length;
				return response;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	public String generateWalletAccountNumber(WalletAccountMaster walletAccountMaster)
	{
		String walletAccounNumberStr = null;
		try 
		{
			walletAccounNumberStr = walletAccountMaster.getStrMccCode() + walletAccountMaster.getStrAccountNumber();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return walletAccounNumberStr;
	}

		@Override
		public List<WalletBalanceResponse> getWalletBalancelist(WalletAccountMaster walletAccountMaster) throws Exception
		{
	        List<WalletAccountMaster> walletBalancelist = walletAccountMasterDao.getWalletBalancelist(walletAccountMaster);
			
			List<WalletBalanceResponse> walletBalanceResponseList = new ArrayList<>();
			for(int i = 0; i < walletBalancelist.size();i++) 
			{
				
				WalletAccountMaster walletAccount = walletBalancelist.get(i);
				
				WalletBalanceResponse mapWalletBalanceToWalletBalanceResponse = mapWalletBalanceToWalletBalanceResponse(walletAccount);
				walletBalanceResponseList.add(mapWalletBalanceToWalletBalanceResponse);
			}
			
			return walletBalanceResponseList;
		}

		private WalletBalanceResponse mapWalletBalanceToWalletBalanceResponse(WalletAccountMaster walletAccount) {
			
			WalletBalanceResponse walletBalanceResponse = new WalletBalanceResponse();
			walletBalanceResponse.setStrParticipantID(walletAccount.getStrParticipantID());
			walletBalanceResponse.setStrAccountType(walletAccount.getStrAccountType());
			walletBalanceResponse.setStrAccountNumber(walletAccount.getStrAccountNumber());
			walletBalanceResponse.setStrWalletAccountNumber(walletAccount.getStrWalletAccountNumber());
			walletBalanceResponse.setStrMccCode(walletAccount.getStrMccCode());
			walletBalanceResponse.setStrPercentage(walletAccount.getStrPercentage());
			walletBalanceResponse.setStrAvailableBalance(walletAccount.getStrAvailableBalance());
			
			return walletBalanceResponse;
		}

		@Override
		public List<WalletBalanceShowResponse> getWalletShowBalance(WalletAccountMaster walletAccountMaster) 
		{
			
            List<WalletAccountMaster> walletBalanceShowlist = walletAccountMasterDao.getWalletShowBalance(walletAccountMaster);
			
			List<WalletBalanceShowResponse> walletBalanceShowResponselist = new ArrayList<>();
			for(int i = 0; i < walletBalanceShowlist.size();i++) 
			{
				
				WalletAccountMaster walletAccount = walletBalanceShowlist.get(i);
				
				WalletBalanceShowResponse mapWalletBalanceShowToWalletBalanceShowResponse = mapWalletBalanceShowToWalletBalanceShowResponse(walletAccount);
				walletBalanceShowResponselist.add(mapWalletBalanceShowToWalletBalanceShowResponse);
			}
			
			return walletBalanceShowResponselist;		
		}

		private WalletBalanceShowResponse mapWalletBalanceShowToWalletBalanceShowResponse(WalletAccountMaster walletAccount) 
		{
			WalletBalanceShowResponse walletBalanceShowResponse=new WalletBalanceShowResponse();
			walletBalanceShowResponse.setStrWalletAccountNumber(walletAccount.getStrWalletAccountNumber());
			walletBalanceShowResponse.setStrPercentage(walletAccount.getStrPercentage());
			walletBalanceShowResponse.setStrAvailableBalance(walletAccount.getStrAvailableBalance());
			return walletBalanceShowResponse;
		}

		@Override
		public List<WalletAccountMaster> getLinkedAccountWalletList(WalletAccountMaster walletAccountMaster) throws Exception
		{
			return walletAccountMasterDao.getLinkedAccountWalletList(walletAccountMaster);
		}
		
		@Override
		public ProcessResponse getWalletShowBalance(WalletAccountMaster walletAccountMaster, ProcessResponse processResponse) throws Exception
		{
            List<WalletAccountMaster> walletBalanceShowlist = walletAccountMasterDao.getWalletShowBalance(walletAccountMaster);
			
            List<WalletBalanceShowResponse> walletBalanceShowResponselist = new ArrayList<>();    			
            if (walletBalanceShowlist!=null && walletBalanceShowlist.size() > 0) 
			{
    			Long totalWalletBalance = 0L;    			
    			for(int i = 0; i < walletBalanceShowlist.size();i++) 
    			{
    				
    				WalletAccountMaster walletAccount = walletBalanceShowlist.get(i);
    					
    				totalWalletBalance = totalWalletBalance + Long.parseLong(walletAccount.getStrAvailableBalance()); 
    				
    				WalletBalanceShowResponse mapWalletBalanceShowToWalletBalanceShowResponse = mapWalletBalanceShowToWalletBalanceShowResponse(walletAccount);
    				walletBalanceShowResponselist.add(mapWalletBalanceShowToWalletBalanceShowResponse);
    			}
    			processResponse.setTotalWalletBalance(""+totalWalletBalance);
            	processResponse.setMessage("Data Fetched Successfully.");				
				processResponse.setWalletBalanceshow(walletBalanceShowResponselist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
				processResponse.setWalletBalanceshow(walletBalanceShowResponselist);
			}			
			return processResponse;		
		}

		//Wallet Percentage Edit By Jyoti S
		public int updateWalletPercentage(WalletAccountMaster walletAccountMaster) throws Exception{
			int update;
			if(!walletAccountMaster.equals(null)) {
				update = walletAccountMasterDao.updateWalletPercentage(walletAccountMaster);
				return update;
			}
			return 0;
		}

}
