package ams.cms;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties 
{
    private String location;
    
    private String tier;
    
    public String getLocation() 
    {
        return location;
    }
    
    public void setLocation(String location) 
    {
        this.location = location;
    }
    
	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}
}