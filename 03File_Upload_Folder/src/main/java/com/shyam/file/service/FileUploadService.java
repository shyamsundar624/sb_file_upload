package com.shyam.file.service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService implements IFileUploadService {

	private final Path rootDir = Paths.get("uploads");

	@Override
	public void init() {
		try {
			File tempDir = new File(rootDir.toUri());
			boolean dirExists = tempDir.exists();
			if (!dirExists) {
				Files.createDirectory(rootDir);
			}

		} catch (Exception e) {
			throw new RuntimeException("Error Crrating Directory");
		}

	}

	@Override
	public void save(MultipartFile file) {

		try {
			Files.copy(file.getInputStream(), this.rootDir.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("Error Uploading File! Try after sometime");
		}
	}

	@Override
	public Resource getFileByName(String fileName) {
		try {
			Path filePath = rootDir.resolve(fileName);
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could Not Read File");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {

		FileSystemUtils.deleteRecursively(rootDir.toFile());
	}

	@Override
	public Stream<Path> loadAllFiles() {
		try {
			return Files.walk(rootDir, 1).filter(path -> !path.equals(this.rootDir)).map(this.rootDir::relativize);
		} catch (Exception e) {
			throw new RuntimeException("Could Not Load Files");
		}
	}

}
