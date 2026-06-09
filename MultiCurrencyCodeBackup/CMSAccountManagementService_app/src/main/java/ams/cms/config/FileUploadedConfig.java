package ams.cms.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FileUploadedConfig 
{
    @Autowired
	Environment environment;
    
    private String qrCodeLocation;
    private String dQrCodeLocation;
    private String qrCodeServerUrl;
    private String serverPort;
    
    @PostConstruct
	public void init()
	{
    	setQrCodeLocation(environment.getProperty("file.qrcode.imagepath"));
    	setdQrCodeLocation(environment.getProperty("file.dqrcode.imagepath"));
    	setQrCodeServerUrl(environment.getProperty("ams.server.url"));
    	setServerPort(environment.getProperty("server.port"));
	}

	public String getQrCodeLocation() {
		return qrCodeLocation;
	}
	public void setQrCodeLocation(String qrCodeLocation) {
		this.qrCodeLocation = qrCodeLocation;
	}
	public String getQrCodeServerUrl() {
		return qrCodeServerUrl;
	}
	public void setQrCodeServerUrl(String qrCodeServerUrl) {
		this.qrCodeServerUrl = qrCodeServerUrl;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getdQrCodeLocation() {
		return dQrCodeLocation;
	}

	public void setdQrCodeLocation(String dQrCodeLocation) {
		this.dQrCodeLocation = dQrCodeLocation;
	}
}
