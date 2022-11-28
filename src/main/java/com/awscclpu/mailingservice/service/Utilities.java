package com.awscclpu.mailingservice.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class Utilities {

	/**
	 * Finds Content type of filename by filenames postfix
	 *
	 * @param filename filename to find
	 * @return MediaType of the given filename
	 */
	public MediaType contentType(String filename) {
		String[] fileArrSplit = filename.split("\\.");
		String fileExtension = fileArrSplit[fileArrSplit.length - 1];
		return switch (fileExtension) {
			case "template", "txt" -> MediaType.TEXT_PLAIN;
			case "png" -> MediaType.IMAGE_PNG;
			case "jpg" -> MediaType.IMAGE_JPEG;
			default -> MediaType.APPLICATION_OCTET_STREAM;
		};
	}

	public boolean verifyTemplate(String templateName) {
		return templateName.endsWith(".template");
	}
}
