package com.udacity.jwdnd.course1.cloudstorage.mapper;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    //CREATE
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata, uploadtime) " +
            "VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData}, #{uploadTime})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    //READ
    @Select("SELECT fileid, filename, filesize FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(Integer fileId);


    //UPDATE
    @Update("UPDATE FILES set filename=#{filename}, contenttype=#{contentType}, filesize=#{fileSize}, " +
            "userid=#{userId}, filedata=#{fileData}, uploadtime=#{uploadTime} WHERE fileid = #{fileId}")
    int updateFile(File file);

    //DELETE
    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteById(Integer fileId);


}
