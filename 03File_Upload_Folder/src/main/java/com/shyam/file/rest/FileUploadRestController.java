package com.shyam.file.rest;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.shyam.file.response.FileResponse;
import com.shyam.file.response.FileResponseMessage;
import com.shyam.file.service.FileUploadService;
import com.shyam.file.service.IFileUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadRestController {

	private final IFileUploadService uploadService;

	@PostMapping("/upload-files")
	public ResponseEntity<FileResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files) {
		String message = null;
		try {
			List<String> fileName = new ArrayList<>();
			Arrays.stream(files).forEach(file -> {
				uploadService.save(file);
				fileName.add(file.getOriginalFilename());
			});
			message = "Files uploaded successfully : " + fileName;
			return ResponseEntity.status(HttpStatus.OK).body(new FileResponseMessage(message));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileResponseMessage(e.getMessage()));
		}
	}

	@GetMapping("/file/{fileName}")
	public ResponseEntity<Resource> getGetFileByName(@PathVariable String fileName) {

		Resource resource = uploadService.getFileByName(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FileResponse>> loadAllFiles() {
		List<FileResponse> list = uploadService.loadAllFiles().map(path -> {
			String fileName = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FileUploadRestController.class, "getGetFileByName", path.getFileName().toString())
					.build().toString();
			return new FileResponse(fileName, url);

		}).toList();

		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	
}
