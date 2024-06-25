package com.shyam.file.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shyam.file.entity.File;
import com.shyam.file.repo.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileRepository fileRepository;

public File storeFile(MultipartFile file) {
	String filename = file.getOriginalFilename();
	try {
		File fileEntity = new File();
		fileEntity.setFilename(filename);
		fileEntity.setFileType(file.getContentType());
		fileEntity.setData(file.getBytes());
		
		return fileRepository.save(fileEntity);
	} catch (IOException e) {
throw new RuntimeException("Could not store file "+filename+" . Please try agaian!",e);
	}
}

public Optional<File> getFile(Long fileId){
	return fileRepository.findById(fileId);
}
}
