package ams.cms.api.handler;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;

import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.BulkTransferDto;
import ams.cms.api.model.BulkTransferExcelReqRes;
import ams.cms.util.ProcessResponse;

public interface BulkTransferHandler 
{
	ProcessResponse bulkTransferOneToMany(BulkTransferDto bulkTransferDto);

	ProcessResponse bulkTransferManyToOne(BulkTransferDto bulkTransferDto);

	ProcessResponse verifyBulkTransferEntries(BulkTransfer bulkTransfer);

	ProcessResponse approveBulkTransfers(BulkTransfer bulkTransfer);

	List<BulkTransfer> getBulkTransfer(BulkTransferDto bulkTransfer);

	List<BulkTransfer> findAll();

	List<BulkTransfer> findAllByPreTransactionId(BulkTransferDto bulkTransferDto);

	List<BulkTransfer> getPreTransactionIdByTxnAmount(BulkTransfer bulkTransfer);

	List<BulkTransfer> getAuthorizeBlukTransferList(BulkTransfer bulkTransfer);	
	
	List<BulkTransfer> getAuthorizePreTxnIdBulklist(BulkTransfer bulkTransfer);
	
	ProcessResponse rejectBulkTransfersWithReason(BulkTransfer bulkTransfer);
	
	ProcessResponse getBulkTransferExcelReqRes(Workbook workbook);
	
	ProcessResponse saveBulkTransferExcelData(BulkTransferDto bulkTransfer);
	
	ResponseEntity<?> getAccountLimitInfo(BulkTransferDto bulkTransfer);

	ResponseEntity<?> getCumlativeBalanceCompareTransAmt(BulkTransferDto bulkTransfer);
	
	List<BulkTransfer> getPreTxnIdBulklistForVerify(BulkTransfer bulkTransfer);
}
