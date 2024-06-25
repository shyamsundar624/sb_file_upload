package com.shyam.file.rest;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shyam.file.service.FileStorageServie;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {

	private final FileStorageServie fileStorageServie;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
		String storeFile = fileStorageServie.storeFile(file);
		return ResponseEntity.ok("File Uploaded Successfully: "+storeFile);
	}
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName){
		Path filePath = fileStorageServie.loadFileResource(fileName);
	Resource resource;
	try {
		resource=new UrlResource(filePath.toUri());
		if(!resource.exists()) {
			throw new RuntimeException("File Not Found "+fileName);
		}
	} catch (Exception e) {
throw new RuntimeException("File Not Found : "+fileName,e);
	}
	return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION,"attachment: filename=\""+resource.getFilename()+"\"")
			.body(resource);
	}
}
