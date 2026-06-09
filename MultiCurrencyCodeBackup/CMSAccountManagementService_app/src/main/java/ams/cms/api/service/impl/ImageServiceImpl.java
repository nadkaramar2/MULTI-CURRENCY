package ams.cms.api.service.impl;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ams.cms.FileUploadProperties;
import ams.cms.api.dao.PreAccountMasterDao;
import ams.cms.api.dao.impl.PreAccountMasterDaoImpl;
import ams.cms.api.service.ImageService;
import ams.cms.config.FileUploadedConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountKycDetails;


//added by ankit
@Transactional
@Service
public class ImageServiceImpl implements ImageService 
{
	
	private AMSLogger amsLogger = AMSLogger.getInstance(PreAccountMasterDaoImpl.class);
	
	@Autowired
	private PreAccountMasterDao preAccountMasterDao;	
	
	@Autowired
	FileUploadedConfig fileUploadedConfig;
	
	private final Path dirLocation;
	
	private final Path dirTierPhotos;
	
    @Autowired
    public ImageServiceImpl(FileUploadProperties fileUploadProperties) 
    {
        this.dirLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
        
        this.dirTierPhotos = Paths.get(fileUploadProperties.getTier()).toAbsolutePath().normalize();
    }
	
	//Will Create the Directory of the File if it is not Present local System
    @Override
    @PostConstruct
    public void init() throws Exception 
    {
        try
        {
            Files.createDirectories(this.dirLocation);
            
            Files.createDirectories(this.dirTierPhotos);
            
        } 
        catch (Exception ex) {
            throw new Exception("Could not create upload dir!");
        }
    }
	
    @Override
    public Path loadDirectory(String fileName) throws FileNotFoundException 
    {
		try 
		{
            Path file = this.dirLocation.resolve(fileName).normalize();
            return file;
		}
        catch (Exception e) {
              throw new FileNotFoundException("Could not fetch file");
         }           
	}
    
    //added by ankit
    @Override
    public Path loadTierDirectory(String fileName) throws FileNotFoundException 
    {
		try 
		{
            Path file = this.dirTierPhotos.resolve(fileName).normalize();
            return file;
		}
        catch (Exception e) {
              throw new FileNotFoundException("Could not fetch file");
         }           
	}
    
    @Override
	public Resource loadTierPhotos(String fileName) throws FileNotFoundException
	{
		try
		{
            Path file = this.dirTierPhotos.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } 
            else {
                throw new FileNotFoundException("Could not find file");
            }
          } 
          catch (MalformedURLException e) {
              throw new FileNotFoundException("Could not fetch file");
          }           
	}
    //added by ankit
    
	@Override
	public Resource loadImage(String fileName) throws FileNotFoundException
	{
		try
		{
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } 
            else {
                throw new FileNotFoundException("Could not find file");
            }
          } 
          catch (MalformedURLException e) {
              throw new FileNotFoundException("Could not fetch file");
          }           
	}
	
	@Override
    public String saveImage(MultipartFile image,String imageName) 
	{
	     try
	     {
	         String fileName = image.getOriginalFilename();
	         String saveImageName = imageName+".jpg";
	         
	         byte[] bytes = image.getBytes();
	         //Path imagePath = this.dirLocation.resolve(fileName);
	         //long copy = Files.copy(file.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);
			 Path imagePath = this.dirLocation.resolve(saveImageName);
			 Files.write(imagePath, bytes);
			 return "Image Could not be Uploaded";
			 //return ResponseHandler.normalResponse("Shop Image Uploaded Succesfully", HttpStatus.OK);
	
	     } 
	     catch (Exception e) {
	    	 return "Image Could not be Uploaded exception occurred::"+e.getMessage();
	     }
	}

	
	@Override
    public int saveOriginalImage(MultipartFile image, String mobileNo) 
	{
	   try 
	   {
	         String imageName = image.getOriginalFilename();
	         String saveImageName = imageName + mobileNo + ".jpg";
	         byte[] bytes = image.getBytes();
	         Path imagePath = this.dirLocation.resolve(saveImageName);
	         long copy = Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
			 return 1;		
	   }
	   catch (Exception e) 
	   {
	    	 e.printStackTrace();
	    	 return 0;
	   }
	}
	
	@Override
    public long saveAddressImage(MultipartFile image,String mobileNo)
	{
	     try 
	     {
	    	 AccountKycDetails kycAddressDocumentType = preAccountMasterDao.getKycAddressDocumentType(mobileNo);    	 
	    	 //String strAddressProofDocumentType = kycAddressDocumentType.getStrAddressProofDocumentType();
	    	 //String prefix = addressProofDocumentTypeDao.getPrefixOfDocumentType(strAddressProofDocumentType);
	    	 String prefix = kycAddressDocumentType.getStrAddressProofDocumentType();
	         String saveImageName = prefix + mobileNo + ".jpg";
	         
	         Path imagePath = this.dirLocation.resolve(saveImageName);
	         long copy = Files.copy(image.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);
	         int addressProofUpload = preAccountMasterDao.addressProofUpload(saveImageName, mobileNo);
			 if(copy > 0 && addressProofUpload > 0){
				 return 1;
			 }
			 else {
				 return 0; 
			 }		
	     } 
	     catch (Exception e) 
	     {
	    	 e.printStackTrace();
	    	 return 0;
	     }
	}
	
	@Override
    public long saveIdentityImage(MultipartFile image,String mobileNo) 
	{
	     try
	     {
	    	 AccountKycDetails kycIdentityDocumentType = preAccountMasterDao.getKycIdentityDocumentType(mobileNo);
	    	 //String strIdentityProofDocumentType = kycIdentityDocumentType.getStrIdentityProofDocumentType();
	    	 //String prefix = identityProofDocumentTypeDao.getPrefixOfDocumentType(strIdentityProofDocumentType);
	    	 String prefix = kycIdentityDocumentType.getStrIdentityProofDocumentType();
	    	 
	         String saveImageName = prefix + mobileNo + ".jpg";
	         Path imagePath = this.dirLocation.resolve(saveImageName);
	         long copy = Files.copy(image.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);
	         int identityProofUpload = preAccountMasterDao.IdentityProofUpload(saveImageName, mobileNo);
			 if(copy > 0 && identityProofUpload > 0)
			 {
				 return 1;
			 }
			 else
			 {
				 return 0; 
			 }
	     } 
	     catch (Exception e) 
	     {
	    	 e.printStackTrace();
	    	 return 0;
	     }
	}

	@Override
	public Resource getDownloadImage(String imageName) throws FileNotFoundException
	{
		try
		{
			Path imagPath = Paths.get(fileUploadedConfig.getQrCodeLocation());			
            Path file = imagPath.resolve(imageName).normalize();
            
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) 
            {
                return resource;
            } 
            else
            {
                throw new FileNotFoundException("Could not find file");
            }
          } 
          catch (MalformedURLException e)
		  {
              throw new FileNotFoundException("Could not fetch file");
          }
	}

	@Override
	public Resource getDownloadedDynamicQrImage(String imageName) throws FileNotFoundException 
	{
		try
		{
			Path imagPath = Paths.get(fileUploadedConfig.getdQrCodeLocation());			
            Path file = imagPath.resolve(imageName).normalize();
            
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) 
            {
                return resource;
            } 
            else
            {
                throw new FileNotFoundException("Could not find file");
            }
          } 
          catch (MalformedURLException e)
		  {
              throw new FileNotFoundException("Could not fetch file");
          }
	}
	//added by ankit on 09-05-2023 -start-
	@Override
	//@Async
	public long saveTier1PassportPhoto(MultipartFile image, String mobileNo) 
	{
		try
	     { 
			 String suffix = "tier1";
	         String saveImageName = mobileNo + suffix +".jpg";
	         
	         amsLogger.writeInfoLog("Inside saveTier1PassportPhoto this.dirTierPhotos:::"+this.dirTierPhotos);
	         
	         Path imagePath = this.dirTierPhotos.resolve(saveImageName);
	         long copy = Files.copy(image.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);
	         amsLogger.writeInfoLog("copy:::"+copy);
	         int tier1PhotoUpload = preAccountMasterDao.saveTier1PassportPhoto(saveImageName, mobileNo);
	         
	         amsLogger.writeInfoLog("tier1PhotoUpload:::"+tier1PhotoUpload);
			 if(copy > 0 && tier1PhotoUpload > 0)
			 {
				 return 1;
			 }
			 else
			 {
				 return 0; 
			 }
	     } 
	     catch (Exception e) 
	     {
	    	 amsLogger.writeExceptionLog("Exception occured in saveTier1PassportPhoto::"+ExceptionUtils.getStackTrace(e));
	    	 return 0;
	     }
	}


	@Override
	public long saveTier2PassportPhoto(MultipartFile image, String mobileNo)
	{
		try
	     { 
			 String suffix = "tier2";
	         String saveImageName = mobileNo + suffix +".jpg";
	         Path imagePath = this.dirTierPhotos.resolve(saveImageName);
	         long copy = Files.copy(image.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);
	         
	         int tier2PhotoUpload = preAccountMasterDao.saveTier2PassportPhoto(saveImageName, mobileNo);
			 if(copy > 0 && tier2PhotoUpload > 0)
			 {
				 return 1;
			 }
			 else
			 {
				 return 0; 
			 }
	     } 
	     catch (Exception e) 
	     {
	    	 e.printStackTrace();
	    	 return 0;
	     }
	}
	//added by ankit on 09-05-2023 -end-
}