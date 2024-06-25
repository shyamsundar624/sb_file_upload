package com.shyam.file.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shyam.file.entity.File;
import com.shyam.file.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileRestController {

	private final FileService fileService;
	
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
		File storeFile = fileService.storeFile(file);
		return ResponseEntity.ok("File Upload Successfully: "+storeFile.getFilename());
		
	}
	
	@GetMapping("/download/{fileId}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable long fileId){
		File file = fileService.getFile(fileId).orElseThrow(()-> new RuntimeException("File Not Found With ID "+fileId));
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(file.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getFilename()+"\"")
				.body(file.getData());
						
	}
}
