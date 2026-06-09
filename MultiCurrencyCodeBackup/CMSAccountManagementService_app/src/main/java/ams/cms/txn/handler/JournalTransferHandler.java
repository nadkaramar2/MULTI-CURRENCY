package ams.cms.txn.handler;

import ams.cms.model.JournalTransfer;

public interface JournalTransferHandler 
{
	JournalTransfer processApproveJournalTransfer(JournalTransfer journalTransfer) throws Exception;
		
	JournalTransfer processRejectJournalTransfer(JournalTransfer journalTransfer) throws Exception;
}
