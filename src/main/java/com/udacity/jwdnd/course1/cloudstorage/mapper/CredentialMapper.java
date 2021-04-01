package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface CredentialMapper {
    //CREATE
    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    //READ
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsByUserId(Integer userId);

    //UPDATE
    @Update("UPDATE CREDENTIALS set url=#{url}, username=#{username}, key=#{key}, " +
            "password=#{password}, userid=#{userId} WHERE credentialid=#{credentialId}")
    int updateCredential(Credential credential);

    //DELETE
    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteById(Integer credentialId);
}