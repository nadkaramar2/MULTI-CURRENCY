package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.CustomerByAccountResponse;

public interface BulkTransferService 
{
	List<CustomerByAccountResponse>	getFromAccountBulkDataList(BulkTransfer bulkTransfer);

	List<CustomerByAccountResponse>	getToAccountBulkDataList(BulkTransfer bulkTransfer);

	List<CustomerByAccountResponse>	getToAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer);
	
	List<CustomerByAccountResponse>	getFromAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer);
	
	int updateRejectReason(BulkTransfer bulkTransferObject);

	int[] batchEntryOfToAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfFromAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList);	
	
	int[] batchEntryOfRjectedToAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfRjectedFromAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfApprovedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfRejectedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfBulkTransfer(List<BulkTransfer> bulkTransfers) throws Exception;
}
