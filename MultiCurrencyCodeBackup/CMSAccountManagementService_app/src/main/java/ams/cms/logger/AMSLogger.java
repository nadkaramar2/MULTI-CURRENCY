package ams.cms.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ams.cms.utility.Utils;
/*
 * Author by Sunny Soni
 */

public class AMSLogger
{
	private static String FILE_NAME;
	//private static final String FILE_PATH = "D:\\ams\\log\\";
	//private static final String FILE_PATH = "/usr/app/log/";
	//private static final String FILE_PATH = "/usr/app/Paths/AMS/ams/log/";
	// private static final String FILE_PATH = "D:\\account-management-dev\\log\\";
	
	 private static final String FILE_PATH = "D:\\ams_reactNativeApp_8285\\log\\";
	//private static final String FILE_PATH = "D:\\ams\\account-management-main\\log\\";
	
	//private static final String FILE_PATH = "D:\\NEW_ECLIPSE\\CMS_AMS_UPDATED_CODE_WORKSPACE_14-JAN-23\\AWS_DEV_QA_WORKSPACE\\DEPLOYABLE_PROJECT_WORKSPACE\\LOG\\";
	
	private Class<?> cls;
	
	private static AMSLogger instance;
	
	private AMSLogger(Class<?> cls) 
	{
		this.cls = cls;
	}
	
	public static AMSLogger getInstance(Class<?> clsInfo) 
	{
		instance = new AMSLogger(clsInfo);
		return instance;
	}
	
	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public void writeInfoLog(Object msg) 
	{
		writeLogs(msg, cls, "[Info]");
	}
	public void writeExceptionLog(Object msg) 
	{
		writeLogs(msg, cls, "[Exception]");
	}
	private void writeLogs(Object msg, Class<?> cls, String msgType) 
	{
		try
		{
			//System.out.println();
			//checkFileSize();
			
			boolean isFolderExist =	createFolderIfNotExist();
			if (isFolderExist)
			{
				//File file = new File(FILE_PATH + FILE_NAME);
				File file =  createNewFile();
				
				Object obj = cls.newInstance();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
				
				LocalDateTime curreDateTime = LocalDateTime.now();
				String format = curreDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
				
				writer.append(format);
				writer.append(" ");
				writer.append(msgType.toUpperCase());
				writer.append(" [");
				writer.append(obj.getClass().getName()+"]  [");
				writer.append(obj.getClass().getSimpleName()+".java] - ");
				writer.append(msg+"\n");
				writer.close();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkFileSize() 
	{
		boolean isRequiredToCreateNewFile = false;
		try 
		{
			File file = new File(FILE_PATH + FILE_NAME);
			if (file.exists()) 
			{
				long bytes = file.length();
				long kilobytes = (bytes / 1024);
	            long megabytes = (kilobytes / 1024);
	            System.out.println("File Size in megabytes::["+megabytes+"]");
	            if (10 < megabytes)
	            {
	            	isRequiredToCreateNewFile = true;
	            }
			}
			else
			{
				isRequiredToCreateNewFile = true;
			}
			System.out.println("isRequiredToCreateNewFile::["+isRequiredToCreateNewFile+"]");
			if (isRequiredToCreateNewFile)
			{
				setNewFile();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setNewFile() 
	{
		String strFileName = "ams-logger_"+Utils.getCurrentDate().getTime();
		FILE_NAME = strFileName+".log";
		System.out.println("File Name::["+FILE_NAME+"]");
	}
	
	private boolean createFolderIfNotExist() 
	{
		try 
		{
			System.out.println("File Path::"+FILE_PATH);
			File file = new File(FILE_PATH);
			if(!file.exists())
			{
				file.mkdirs();
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void deleteExistingLogFile() 
	{
		try 
		{
			File file =	new File(FILE_PATH);
			if (file.exists()) 
			{
				File[] files = file.listFiles();
				
				if (files.length == 0)
				{
					return;
				}
				
				for (File f:files) 
				{
					if (f.isFile() && f.exists())
					{
						f.delete();
					}
					else
					{
						//System.out.println("cant delete a file due to open or error");
					}
				}
			}
			else
			{
				//System.out.println("Folder not exist");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private File createNewFile() 
	{
		File f = new File(FILE_PATH);
		try 
		{
			String fileName = getExistingFile(f);
			if(fileName != null && fileName.trim().length() > 0 ) 
			{
				File existFile = new File(FILE_PATH + fileName);
				return existFile;
			}
			
			String strFileName = "ams-logger_"+new Date().getTime()+".log";
			System.out.println("File Name::["+strFileName+"]");
			
			f = new File(FILE_PATH + strFileName);
			if(f.createNewFile()) 
			{
				System.out.println("File created: " + f.getName());
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return f;
	}
	private String getExistingFile(File f) 
	{
		String fileName = "";
		try 
		{
			fileName = getExistingFileName(f);
			if (fileName != null && fileName.trim().length() > 0) 
			{
				long fileSize = getFileSize(fileName);
				System.out.println("File Size::"+fileSize);
				
				boolean fileSizeMore = isFileSizeMoreThan10MB(fileSize);
				System.out.println("fileSizeMore::["+fileSizeMore+"]");
				
				if (fileSizeMore) 
				{
					File file = new File(FILE_PATH + fileName);
					if(file != null) 
					{
						file.delete();
						fileName = "";
						System.out.println("Existing file deleted!!!");
					}
				}
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return fileName;
	}
	private String getExistingFileName(File file) 
	{
		String fileName = "";
		try 
		{
			if(file.exists()) 
			{
				File[] files = file.listFiles();
				if (files.length > 0)
				{
					for (File f:files) 
					{
						fileName = f.getName();
						System.out.println("File Name::"+fileName);
						break;
					}
				}
			}
			else
			{
				System.out.println("File not exist");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return fileName;
	}
	private long getFileSize(String fileName) 
	{
		try 
		{
			File file = new File("E:\\account-management\\log\\"+fileName);
			if(file != null) 
			{
				return file.length();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return 0l;
	}
	private boolean isFileSizeMoreThan10MB(long fileSize) 
	{
		boolean isFileSizeMoreThan10MB = false;
		try 
		{
			double kilobytes = (fileSize / 1024);
			double megabytes = (kilobytes / 1024);
            
            int mb = (int) Math.rint(megabytes);
            
            System.out.println("mb=["+mb+"]");
            if (10 < mb)
            {
            	isFileSizeMoreThan10MB = true;
            }
		}
		catch (Exception e) {
		}
		return isFileSizeMoreThan10MB;
	}
}
