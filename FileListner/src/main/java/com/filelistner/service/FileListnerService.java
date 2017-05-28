package com.filelistner.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filelistner.entity.FileEntity;
import com.filelistner.repository.FileListnerRepo;

@Service
public class FileListnerService {
	
	@Autowired
	private FileListnerRepo repo;
	
	public void fileListner(){
		try(WatchService watcher = FileSystems.getDefault().newWatchService();) {
			
			//WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get("D:/FileListner");

			dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);

			while (true) {
				WatchKey key;
				ExecutorService executor = Executors.newFixedThreadPool(10);
				try {
					// wait for a key to be available
					key = watcher.take();
				} catch (InterruptedException ex) {
					break;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					// get event type
					WatchEvent.Kind<?> kind = event.kind();

					// get file name
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filecontext = ev.context();

					System.out.println(kind.name() + ": " + filecontext);

					if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
						String fileName = filecontext.getFileName().toString();
						executor.submit(new Runnable() {
							
							@Override
							public void run() {
								FileEntity file =  new FileEntity();
								
								try(InputStream inputStream = new FileInputStream("D:/FileListner/"+fileName);) {
									//inputStream = new FileInputStream("D:/FileListner/"+fileName);
									
									file.setFileName(fileName);
									file.setInputStream(inputStream);
									System.out.println("inside");
									
									//insert to DB
									FileEntity filenew = repo.insert(file);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								//System.out.println("filenew"+filenew.getFileName());
							}
						});
						

					}
				}

				// IMPORTANT: The key must be reset after processed
				boolean valid = key.reset();
				executor.shutdown();
				if (!valid) {
					break;
				}
			}
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	private void insertToDB(String fileName) throws FileNotFoundException{
		FileEntity file =  new FileEntity();
		InputStream inputStream = new FileInputStream("D:/FileListner/"+fileName);
		file.setFileName(fileName);
		file.setInputStream(inputStream);
		
		//insert to DB
		repo.insert(file);
	}
	

}
