package ams.cms.utility;

import org.springframework.core.io.Resource;

public class ImageResponse {
	private String name;
	private Resource resource;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "ImageResponse [name=" + name + ", resource=" + resource + "]";
	}
	
}
