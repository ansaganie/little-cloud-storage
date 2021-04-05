package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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

    public int update(Note note, Integer userId) {
        Note existing = noteMapper.getNoteById(note.getNoteId());
        if (existing.getNoteId().equals(userId)) return noteMapper.updateNote(note);
        else return 0;
    }

    public int deleteById(Integer id, Integer userId) {
        return noteMapper.deleteById(id, userId);
    }
}
