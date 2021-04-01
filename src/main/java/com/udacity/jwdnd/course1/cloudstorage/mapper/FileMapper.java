package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface FileMapper {
    //CREATE
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    //READ
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    //UPDATE
    @Update("UPDATE FILES set filename=#{filename}, contenttype=#{contentType}, filesize=#{fileSize}, " +
            "userid=#{userId}, filedata=#{fileData} WHERE fileid = #{fileId}")
    int updateCredential(File file);

    //DELETE
    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteById(Integer fileId);
}
