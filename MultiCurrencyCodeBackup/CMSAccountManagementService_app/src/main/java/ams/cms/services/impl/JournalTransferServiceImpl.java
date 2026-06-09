package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.dao.JournalTransferDao;
import ams.cms.model.JournalTransfer;
import ams.cms.services.JournalTransferService;
import ams.cms.utility.Utils;

//created by ankit
@Transactional
@Service
public class JournalTransferServiceImpl implements JournalTransferService{

	@Autowired
	private JournalTransferDao journalTransferDao;
	
	@Autowired
	TransactionIdCreationConfigDao transactionIdCreationConfigDao; 
	
	@Override
	public JournalTransfer addJournalTransferEntry(JournalTransfer journalTransfer) 
	{
		try
		{
			String transactionId = transactionIdCreationConfigDao.getTransactionId();
			journalTransfer.setStrTxnId(transactionId);
			journalTransfer.setStrTxnDate(Utils.getCurrentDate());
			journalTransfer.setStrTxnTime(Utils.getFormattedCurrentTime());
			journalTransfer.setStrTxnStatus("pending");
			journalTransferDao.save(journalTransfer);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return journalTransfer;
	}
	
	@Override
	public List<JournalTransfer> getAccountVeriflylist(JournalTransfer journalTransfer) 
	{
		return journalTransferDao.getAccountVeriflylist(journalTransfer);	
	}

	@Override
	public List<JournalTransfer> getTxnIdApprovalInfo(JournalTransfer journalTransfer)
	{
		return journalTransferDao.getTxnIdApprovalInfo(journalTransfer);	
	}

	@Override
	public int getUpdateReasonlist(JournalTransfer journalTransfer) 
	{
		return journalTransferDao.getUpdateReasonlist(journalTransfer);	
	}

	@Override
	public int updateJournalTransferTxnStatus(JournalTransfer journalTransfer) throws Exception 
	{
		return journalTransferDao.updateJournalTransferTxnStatus(journalTransfer);
	}
	
	@Override
	public List<JournalTransfer> getTxnJournalReport(JournalTransfer journalTransfer) {
		return journalTransferDao.getTxnJournalReport(journalTransfer);

	}
	
	
}
