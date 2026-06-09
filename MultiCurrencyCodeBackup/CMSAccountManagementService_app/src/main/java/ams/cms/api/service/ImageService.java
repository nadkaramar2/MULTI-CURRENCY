package ams.cms.api.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
//added by ankit
public interface ImageService {

	void init() throws Exception;
	
	String saveImage(MultipartFile image, String imageName);

	Resource loadImage(String imageName) throws FileNotFoundException;

	int saveOriginalImage(MultipartFile image,String mobileNo);

	Path loadDirectory(String imageName) throws FileNotFoundException;

	long saveIdentityImage(MultipartFile image, String mobileNo);

	long saveAddressImage(MultipartFile image, String mobileNo);
	
	Resource getDownloadImage(String imageName) throws FileNotFoundException;
	
	Resource getDownloadedDynamicQrImage(String imageName) throws FileNotFoundException;
	
	Path loadTierDirectory(String fileName) throws FileNotFoundException;
	
	Resource loadTierPhotos(String fileName) throws FileNotFoundException;
	
	//added by ankit on 09-05-2023   ---edited to save for participantId --India-profile photo  //,String participantId
	long saveTier1PassportPhoto(MultipartFile image, String mobileNo );
	
	long saveTier2PassportPhoto(MultipartFile image, String mobileNo);
	//added by ankit on 09-05-2023
}
