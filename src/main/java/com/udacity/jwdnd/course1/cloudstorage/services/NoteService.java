package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int save(Note note) {
        return noteMapper.insert(note);
    }

    public List<Note> getAllNotesByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public Note getNoteById(Integer id) {
        return noteMapper.getNoteById(id);
    }

    public int update(Note note) {
        return noteMapper.updateNote(note);
    }

    public int deleteById(Integer id) {
        return noteMapper.deleteById(id);
    }
}
