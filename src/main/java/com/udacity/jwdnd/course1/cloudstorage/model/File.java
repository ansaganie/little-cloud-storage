package com.udacity.jwdnd.course1.cloudstorage.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class File {
    private Integer fileId;
    private String filename;
    private String contentType;
    private Long fileSize;
    private Integer userId;
    private byte[] fileData;
    private Date uploadTime;

    public File(Integer fileId,
                String filename,
                String contentType,
                Long fileSize,
                Integer userId,
                byte[] fileData, Date uploadTime) {
        this.fileId = fileId;
        this.filename = filename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;
        this.fileData = fileData;
        this.uploadTime = uploadTime;
    }

    public File() {

    }

    public Integer getFileId() {
        return fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileId=" + fileId +
                ", filename='" + filename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileSize=" + fileSize +
                ", userId=" + userId +
                ", uploadTime=" + uploadTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return Objects.equals(getFileId(), file.getFileId()) && Objects.equals(getFilename(), file.getFilename()) && Objects.equals(getContentType(), file.getContentType()) && Objects.equals(getFileSize(), file.getFileSize()) && Objects.equals(getUserId(), file.getUserId()) && Arrays.equals(getFileData(), file.getFileData()) && Objects.equals(getUploadTime(), file.getUploadTime());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getFileId(), getFilename(), getContentType(), getFileSize(), getUserId(), getUploadTime());
        result = 31 * result + Arrays.hashCode(getFileData());
        return result;
    }
}
