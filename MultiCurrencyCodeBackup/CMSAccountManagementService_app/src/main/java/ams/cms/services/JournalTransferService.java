package ams.cms.services;

import java.util.List;
import ams.cms.model.JournalTransfer;

//created by ankit
public interface JournalTransferService
{
	JournalTransfer addJournalTransferEntry(JournalTransfer journalTransfer);
	
	List<JournalTransfer> getAccountVeriflylist(JournalTransfer journalTransfer);

	List<JournalTransfer> getTxnIdApprovalInfo(JournalTransfer journalTransfer);

	int getUpdateReasonlist(JournalTransfer journalTransfer);
	
	int updateJournalTransferTxnStatus(JournalTransfer journalTransfer) throws Exception;
	
	List<JournalTransfer> getTxnJournalReport(JournalTransfer journalTransfer);
}
