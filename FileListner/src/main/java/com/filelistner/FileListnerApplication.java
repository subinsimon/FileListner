
package com.filelistner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.filelistner.service.FileListnerService;

@SpringBootApplication
public class FileListnerApplication implements CommandLineRunner{
	
	@Autowired
	private FileListnerService s;

	public static void main(String[] args) {
		SpringApplication.run(FileListnerApplication.class, args);
		
			
	}
	

	@Override
	public void run(String... arg0) throws Exception {
		s.fileListner();
		
	}
}
