package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.dao.GenericDao;

public interface BulkTransferDao extends GenericDao<BulkTransfer>
{
	List<BulkTransfer> findByTransactionId(BulkTransfer bulkTransfer);

	List<BulkTransfer> findByPreTransactionSerialId(BulkTransfer bulkTransfer);

	List<BulkTransfer> findOneByPreTransactionSerialId();

	List<BulkTransfer> findOneByPreTransactionSerialId(BulkTransfer bulkTransfer);

	List<BulkTransfer> findAllByPreTransactionId(BulkTransfer bulkTransfer);

	int updateRejectReason(BulkTransfer bulkTransferObject);
	
	void updateVerifiedObject(BulkTransfer bulkTransfer);

	List<BulkTransfer> getPreTransactionIdByTxnAmount(BulkTransfer bulkTransfer);

	void updateSuccessStatus(BulkTransfer bulkTransfer);

	List<BulkTransfer> getAuthorizeBlukTransferList(BulkTransfer bulkTransfer);
	
	List<CustomerByAccountResponse>	getFromAccountBulkDataList(BulkTransfer bulkTransfer);

	List<CustomerByAccountResponse>	getToAccountBulkDataList(BulkTransfer bulkTransfer);
	
	List<CustomerByAccountResponse>	getToAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer);
	
	List<CustomerByAccountResponse>	getFromAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer);
	
	int[] batchEntryOfToAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfFromAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfRjectedToAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfRjectedFromAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfApprovedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList);
	
	int[] batchEntryOfRejectedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList);
	
	List<BulkTransfer> getAuthorizePreTxnIdBulklist(BulkTransfer bulkTransfer);
	
	int[] batchEntryOfBulkTransfer(List<BulkTransfer> bulkTransfers);
	
	List<BulkTransfer> getPreTxnIdBulklistForVerify(BulkTransfer bulkTransfer);
}
