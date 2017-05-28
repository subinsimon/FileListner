package com.filelistner.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.filelistner.entity.FileEntity;

@Repository
public interface FileListnerRepo{
	
	public FileEntity insert(FileEntity file);

}
