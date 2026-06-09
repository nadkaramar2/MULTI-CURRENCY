package ams.cms.api.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ams.cms.api.service.ImageService;
import ams.cms.logger.AMSLogger;
import ams.cms.utility.ImageResponse;
import org.springframework.http.MediaType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

//added by ankit
@RestController
@RequestMapping("/image")
public class ImageController
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ImageController.class);
	
	@Autowired
	private ImageService imageService;
	
	//API to Display a Particular Image
	@GetMapping("/display/{imageName:.+}")
    public ResponseEntity<?> displayImage(@PathVariable String imageName) throws IOException 
	{
    	try
    	{
    		Resource resource = imageService.loadImage(imageName);
            String fetchedImageName = resource.getFilename();
            ImageResponse imageResponse = new ImageResponse();
            imageResponse.setName(fetchedImageName);
            imageResponse.setResource(resource);
            /*String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/display/")
                    .path(fetchedImageName)
                    .toUriString(); */
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
    				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fetchedImageName + "\"")
    				.body(resource );
    	}
    	catch(Exception e)
    	{
    		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
    		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
    				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + "notFound" + "\"")
    				.body("Broken Image");
    	}
    }
	
	//Get download QR code image Start
	@GetMapping("/qrcode/download/{imageName:.+}")
    public ResponseEntity<?> getDownloadQrCodeImage(@PathVariable String imageName) throws IOException
	{
    	try
    	{
    		Resource resource = imageService.getDownloadImage(imageName);
            String fetchedImageName = resource.getFilename();
            ImageResponse imageResponse = new ImageResponse();
            imageResponse.setName(fetchedImageName);
            imageResponse.setResource(resource);
            
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fetchedImageName + "\"").body(resource );
    	}
    	catch(Exception e) 
    	{
    		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
    		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + "notFound" + "\"").body("Broken Image");
    	}
    }
	//Get download QR code image End
	//Get download Dynamic QR code image Start
	@GetMapping("/dqrcode/download/{imageName:.+}")
    public ResponseEntity<?> getDownloadedDynamicQrCodeImage(@PathVariable String imageName) throws IOException
	{
    	try
    	{
    		Resource resource = imageService.getDownloadedDynamicQrImage(imageName);
            String fetchedImageName = resource.getFilename();
            ImageResponse imageResponse = new ImageResponse();
            imageResponse.setName(fetchedImageName);
            imageResponse.setResource(resource);
            
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fetchedImageName + "\"").body(resource );
    	}
    	catch(Exception e) 
    	{
    		amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
    		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + "notFound" + "\"").body("Broken Image");
    	}
    }
	//Get download Dynamic QR code image End
	
	 @RequestMapping(path = "/upload", method = RequestMethod.POST)
	 public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image, @RequestParam("emailId") String emailId, @RequestParam("mobileNo") String mobileNo) throws FileNotFoundException
	 {
		imageService.saveOriginalImage(image,mobileNo);
		String originalFilename = image.getOriginalFilename();
		Path loadDirectory = imageService.loadDirectory(originalFilename);
		/*
		 * String fileDisplayUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
		 * .path("/display/") .path(originalFilename) .toUriString();
		 */
		return ResponseEntity.ok(loadDirectory);
	}
}
