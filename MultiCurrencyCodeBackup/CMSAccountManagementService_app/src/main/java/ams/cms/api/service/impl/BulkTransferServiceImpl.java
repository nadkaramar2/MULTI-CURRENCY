package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.BulkTransferDao;
import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.service.BulkTransferService;

@Transactional
@Service
public class BulkTransferServiceImpl implements BulkTransferService 
{
	@Autowired
	BulkTransferDao bulkTransferDao;

	@Override
	public List<CustomerByAccountResponse> getFromAccountBulkDataList(BulkTransfer bulkTransfer) {
		return bulkTransferDao.getFromAccountBulkDataList(bulkTransfer);
	}

	@Override
	public List<CustomerByAccountResponse> getToAccountBulkDataList(BulkTransfer bulkTransfer) 
	{
		return bulkTransferDao.getToAccountBulkDataList(bulkTransfer);
	}

	@Override
	public List<CustomerByAccountResponse> getToAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer) {
		return bulkTransferDao.getToAccountBulkDataListForNonNigeria(bulkTransfer);
	}

	@Override
	public List<CustomerByAccountResponse> getFromAccountBulkDataListForNonNigeria(BulkTransfer bulkTransfer) 
	{
		return bulkTransferDao.getFromAccountBulkDataListForNonNigeria(bulkTransfer);
	}

	@Override
	public int[] batchEntryOfToAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		return bulkTransferDao.batchEntryOfToAccountUpdateBulkTransfer(updatableBulkTransferList);
	}

	@Override
	public int[] batchEntryOfRjectedToAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		return bulkTransferDao.batchEntryOfRjectedToAccountBulkTransfer(updatableBulkTransferList);
	}

	@Override
	public int updateRejectReason(BulkTransfer bulkTransferObject) 
	{
		return bulkTransferDao.updateRejectReason(bulkTransferObject);
	}

	@Override
	public int[] batchEntryOfFromAccountUpdateBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		return bulkTransferDao.batchEntryOfFromAccountUpdateBulkTransfer(updatableBulkTransferList);
	}

	@Override
	public int[] batchEntryOfRjectedFromAccountBulkTransfer(List<BulkTransfer> updatableBulkTransferList) 
	{
		return bulkTransferDao.batchEntryOfRjectedFromAccountBulkTransfer(updatableBulkTransferList);
	}

	@Override
	public int[] batchEntryOfApprovedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList) 
	{
		return bulkTransferDao.batchEntryOfApprovedStatusOfBulkTransferData(updatableBulkTransferList);
	}

	@Override
	public int[] batchEntryOfRejectedStatusOfBulkTransferData(List<BulkTransfer> updatableBulkTransferList) {
		return bulkTransferDao.batchEntryOfRejectedStatusOfBulkTransferData(updatableBulkTransferList);
	}

	@Override
	public int[] batchEntryOfBulkTransfer(List<BulkTransfer> bulkTransfers) throws Exception 
	{
		return bulkTransferDao.batchEntryOfBulkTransfer(bulkTransfers);
 	}
}
