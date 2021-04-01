package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface NoteMapper {
    //CREATE
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) " +
            "VALUES(#{noteTitle}, #{nodeDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    //READ
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    //UPDATE
    @Update("UPDATE NOTES set notetitle=#{noteTitle}, notedescription=#{nodeDescription}," +
            " userid=#{userId} WHERE noteid = #{noteId}")
    int updateCredential(Note note);

    //DELETE
    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteById(Integer noteId);
}
