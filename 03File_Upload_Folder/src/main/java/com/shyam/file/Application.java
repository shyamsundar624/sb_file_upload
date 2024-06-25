package com.shyam.file;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shyam.file.service.IFileUploadService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner{

	@Resource
	private IFileUploadService fileUploadService;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileUploadService.init();
	}

}
