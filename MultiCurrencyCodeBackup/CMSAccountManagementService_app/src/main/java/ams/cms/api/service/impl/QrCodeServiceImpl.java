/*Added by pankaj Pawar for QR CODE Generation*/

package ams.cms.api.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import ams.cms.api.dao.DynamicQrGenerationDao;
import ams.cms.api.dao.QrCodeDao;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.QrInfo;
import ams.cms.api.service.QrCodeService;
import ams.cms.config.FileUploadedConfig;

@Transactional
@Service
public class QrCodeServiceImpl implements QrCodeService {
	
	@Autowired
	QrCodeDao qrCodeDao;
	
	@Autowired
	FileUploadedConfig fileUploadedConfig;
	
	@Autowired
	DynamicQrGenerationDao dynamicQrGenerationDao;
	
	
	@Override
	public int updateQrCodetoAccount(QrInfo qrInfo) throws FileNotFoundException, IOException 
	{
		try
		{
			String fileName = qrInfo.getAccount_no()+"_qrImg.png";
			
			StringBuilder filePathSb = new StringBuilder(fileUploadedConfig.getQrCodeLocation());
			
			File folder = new File(filePathSb.toString());
			if (!folder.exists()) 
			{
				folder.mkdirs();
			}
			
			filePathSb.append("\\");
			filePathSb.append(fileName);			
			qrInfo.setFilePath(filePathSb.toString());
			
			StringBuilder qrCodeImgUrlSb = new StringBuilder(fileUploadedConfig.getQrCodeServerUrl());
			qrCodeImgUrlSb.append(":");
			qrCodeImgUrlSb.append(fileUploadedConfig.getServerPort());
			qrCodeImgUrlSb.append("/AccountManagementAPI/image/qrcode/download/");
			qrCodeImgUrlSb.append(fileName);
			
			qrInfo.setQrCodeImageUrl(qrCodeImgUrlSb.toString());
			
			/*
			String qrInfoImage = getBase64EncodedQrCode(qrInfo);
			qrInfo.setBase64Image(qrInfoImage);
			*/
			getBase64EncodedQrCode(qrInfo);
			
			writeBase64ImageOnMentionedFilepath(qrInfo);
			
			return qrCodeDao.updateQrCodetoAccount(qrInfo);
			//return 1;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
    //Change return Type of method as Object 
	@Override
	public QrInfo getBase64EncodedQrCode(QrInfo qrInfo)
	{
		String base64Image = "";
		try
		{
			StringBuilder qrCodeData = new StringBuilder();
			qrCodeData.append(qrInfo.getAccount_no());
			qrCodeData.append("-");
			qrCodeData.append(qrInfo.getMobile_no());
			qrCodeData.append("-");
			qrCodeData.append(qrInfo.getAccount_name());
			
			int imageSize = 200;
	        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData.toString(), BarcodeFormat.QR_CODE, imageSize, imageSize);
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        MatrixToImageWriter.writeToStream(matrix, "png", bos);
	        /*
		        base64Image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
		        return base64Image;
	        */
	        
	        String actualBase64Image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
	        base64Image = Base64.getEncoder().encodeToString(qrCodeData.toString().getBytes());
	        
	        qrInfo.setActualBase64Image(actualBase64Image);
	        qrInfo.setBase64Image(base64Image);
	        return qrInfo;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//return base64Image;
		return qrInfo;
	}

	@Override
	public void writeBase64ImageOnMentionedFilepath(QrInfo qrInfo) throws Exception
	{
		//byte[] data = Base64.getDecoder().decode(qrInfo.getBase64Image());
		byte[] data = Base64.getDecoder().decode(qrInfo.getActualBase64Image());
        try (OutputStream stream = new FileOutputStream(qrInfo.getFilePath())) 
        {
            stream.write(data);
        }
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code
	@Override
	public void writeDynamicQrBase64ImageOnFilepath(DynamicQrGeneration dynamicQrGeneration) throws Exception
	{
		byte[] data = Base64.getDecoder().decode(dynamicQrGeneration.getActualBase64Img());
        try (OutputStream stream = new FileOutputStream(dynamicQrGeneration.getQrPath())) 
        {
        		stream.write(data);
        }
	}

	//Added by Pankaj Pawar for dynamic QrGeneration code
	@Override
	public DynamicQrGeneration getBase64DynamicQrCode(DynamicQrGeneration dynamicQrGeneration)
	{
		try
		{
			String base64ImageDefault = dynamicQrGeneration.getQrData();
			byte[] decodedBytes = Base64.getDecoder().decode(base64ImageDefault);
			String decodedString = new String(decodedBytes);			
			System.out.println("getBase64DynamicQrCode decodedString:::::["+decodedString+"]");
			
			StringBuilder qrCodeData = new StringBuilder(decodedString);
			qrCodeData.append("-");
			qrCodeData.append(dynamicQrGeneration.getRefNo());
			qrCodeData.append("-");
			qrCodeData.append(dynamicQrGeneration.getAmount());			
			
			int imageSize = 200;
	        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData.toString(), BarcodeFormat.QR_CODE, imageSize, imageSize);
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        MatrixToImageWriter.writeToStream(matrix, "png", bos);
	        String actualBase64Image = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
	        dynamicQrGeneration.setActualBase64Img(actualBase64Image);
	        
	        String base64EncodedStr = Base64.getEncoder().encodeToString(qrCodeData.toString().getBytes());
	        dynamicQrGeneration.setQrCode(base64EncodedStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dynamicQrGeneration;
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code [Start]
	@Override
	public int insertEntryforDynamicQr(DynamicQrGeneration dynamicQrGeneration) 
	{
		dynamicQrGenerationDao.save(dynamicQrGeneration);
		return Integer.parseInt(dynamicQrGeneration.getStrID());
	}
	
	@Override
	public int updateDynamicQrCode(DynamicQrGeneration dynamicQrGeneration) throws FileNotFoundException, IOException {
		try
		{
			String fileName = dynamicQrGeneration.getRefNo()+"_dQRImg.png";
			
			StringBuilder filePathSb = new StringBuilder(fileUploadedConfig.getdQrCodeLocation());
			
			File folder = new File(filePathSb.toString());
			if (!folder.exists()) 
			{
				folder.mkdirs();
			}
			
			filePathSb.append("\\");
			filePathSb.append(fileName);			
			dynamicQrGeneration.setQrPath(filePathSb.toString());
			
			StringBuilder qrCodeImgUrlSb = new StringBuilder(fileUploadedConfig.getQrCodeServerUrl());
			qrCodeImgUrlSb.append(":");
			qrCodeImgUrlSb.append(fileUploadedConfig.getServerPort());
			qrCodeImgUrlSb.append("/AccountManagementAPI/image/dqrcode/download/");//
			qrCodeImgUrlSb.append(fileName);
			
			dynamicQrGeneration.setStrQrCodeImageUrl(qrCodeImgUrlSb.toString());
			
			getBase64DynamicQrCode(dynamicQrGeneration);
			
			writeDynamicQrBase64ImageOnFilepath(dynamicQrGeneration);
			
			return dynamicQrGenerationDao.updateDynamicQrCode(dynamicQrGeneration);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code [Start]
	@Override
	public DynamicQrGeneration getDynamicQrCodeImageUrl(DynamicQrGeneration dynamicQrGeneration) 
	{
		try
		{
			insertEntryforDynamicQr(dynamicQrGeneration);
			int update = updateDynamicQrCode(dynamicQrGeneration);
			if(update > 0)
			{
				return dynamicQrGeneration;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	//Added by Pankaj Pawar for dynamic QrGeneration code[End]
	@Override
	public int updateQrCodeFieldsAfterTxn(DynamicQrGeneration dynamicQrGeneration) throws FileNotFoundException, IOException 
	{
		return dynamicQrGenerationDao.updateQrCodeFieldsAfterTxn(dynamicQrGeneration);
	}
}
