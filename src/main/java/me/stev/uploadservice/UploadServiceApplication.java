package me.stev.uploadservice;

import me.stev.uploadservice.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

@SpringBootApplication
@EnableAsync
public class UploadServiceApplication implements CommandLineRunner {

	@Resource
	private FileStorageService service;

	public static void main(String[] args) {
		SpringApplication.run(UploadServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.deleteAll();
		service.init();
	}
}
