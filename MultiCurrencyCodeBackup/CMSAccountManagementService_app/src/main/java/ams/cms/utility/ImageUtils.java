package ams.cms.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ImageUtils {

	public static String generateImageUrl(String filename) {
		String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/display/")
                .path(filename)
                .toUriString();
		return imageUrl;
	}
	
}
