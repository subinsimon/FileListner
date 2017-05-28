package com.filelistner.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.filelistner.entity.FileEntity;

@Repository
public class FileListnerRepoImpl implements FileListnerRepo {
	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	public FileEntity insert(FileEntity file){
		gridFsTemplate.store(file.getInputStream(), file.getFileName());
		return null;
	}
}
