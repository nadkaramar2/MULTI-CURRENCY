/*Added by pankaj Pawar for QR CODE Generation*/

package ams.cms.api.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.QrInfo;

public interface QrCodeService 
{
	QrInfo getBase64EncodedQrCode(QrInfo qrInfo);
	
	int updateQrCodetoAccount(QrInfo qrInfo) throws FileNotFoundException, IOException; 
	
	void writeBase64ImageOnMentionedFilepath(QrInfo qrInfo) throws Exception;
	
	DynamicQrGeneration getBase64DynamicQrCode(DynamicQrGeneration dynamicQrGeneration);
	
	int insertEntryforDynamicQr(DynamicQrGeneration dynamicQrGeneration);
	
	int updateDynamicQrCode(DynamicQrGeneration dynamicQrGeneration) throws FileNotFoundException, IOException;
	
	public void writeDynamicQrBase64ImageOnFilepath(DynamicQrGeneration dynamicQrGeneration) throws Exception;
	
	DynamicQrGeneration getDynamicQrCodeImageUrl(DynamicQrGeneration dynamicQrGeneration);
	////Added by Pankaj Pawar for dynamic QrGeneration code
	
	int updateQrCodeFieldsAfterTxn(DynamicQrGeneration dynamicQrGeneration) throws FileNotFoundException, IOException;
}
