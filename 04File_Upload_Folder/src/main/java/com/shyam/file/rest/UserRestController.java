package com.shyam.file.rest;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shyam.file.model.User;

@RestController
public class UserRestController {
	private final Path root = Paths.get("upload");

	@PostMapping("/user")
	public ResponseEntity<String> createUser(@RequestBody User user) {
		System.out.println(user);
		// save Logic

		return new ResponseEntity<>("User Saved", HttpStatus.OK);
	}

	@PostMapping("/upload")
	public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file) {

		try {
			Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("User Saved", HttpStatus.OK);
	}

	@PostMapping("/user-profile")
	public ResponseEntity<String> userCreation(@RequestParam("user") String user,
			@RequestParam("file") MultipartFile file) {
		System.out.println(user);
		try {
			Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("User Profile is Created", HttpStatus.OK);
	}
	
	@GetMapping("/file/{filename}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException{
		Path path = this.root.resolve(filename);
		
		Resource file=new UrlResource(path.toUri());
		if(file.exists()) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+file.getFilename())
					.body(file);
		}else {
			throw new RuntimeException("File Not Found");
		}
	}
}
