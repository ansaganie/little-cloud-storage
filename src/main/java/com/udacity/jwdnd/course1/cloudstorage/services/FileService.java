package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int upload(File uploadFile) {
        return fileMapper.insert(uploadFile);
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public int deleteById(Integer fileId, Integer userId) {
        return fileMapper.deleteById(fileId, userId);
    }

    public File getFileById(Integer fileId, Integer userId) {
        return fileMapper.getFileById(fileId, userId);
    }

    public boolean fileExists(String filename, Integer userId, Long filesize) {
        File file = fileMapper.getFileByFilenameAndUserId(filename, userId, filesize);
        return file != null;
    }
}
