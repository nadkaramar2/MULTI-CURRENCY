package ams.cms.dao;

import java.util.List;

import ams.cms.model.JournalTransfer;

public interface JournalTransferDao extends GenericDao<JournalTransfer>
{
	List<JournalTransfer> getAccountVeriflylist(JournalTransfer journalTransfer);

	List<JournalTransfer> getTxnIdApprovalInfo(JournalTransfer journalTransfer);

	int getUpdateReasonlist(JournalTransfer journalTransfer);
	
	int updateJournalTransferTxnStatus(JournalTransfer journalTransfer);
	
	List<JournalTransfer> getTxnJournalReport(JournalTransfer journalTransfer);
}
