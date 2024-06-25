package com.shyam.file.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServie {

	private final Path fileStorageLocation;

	public FileStorageServie(@Value("${file.upload-dir}") String uploadDir) {
		this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new RuntimeException("Could not create the directory where the uploaded will be Stored: " + e);
		}
	}

	public String storeFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			if (fileName.contains("..")) {
				throw new RuntimeException("Invalid Path Sequence " + fileName);
			}
			Path targetPath = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (Exception e) {
			throw new RuntimeException("Could Not Store File " + fileName + " Please try again! " + e);
		}
	}
	
	public Path loadFileResource(String fileName) {
		return this.fileStorageLocation.resolve(fileName).normalize();
	}
	
	public Path loadFileASResource(String fileName) {
		return this.fileStorageLocation.resolve(fileName).normalize();
	}
}
