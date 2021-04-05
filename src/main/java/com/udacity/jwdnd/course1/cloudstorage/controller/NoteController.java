package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String saveNote(@ModelAttribute("note") Note note, Authentication authentication,
                           RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        if (note == null) {
            ra.addFlashAttribute("noteSaveError", true);
        } else {
            if (note.getNoteId() == null) {
                int noteId;
                note.setUserId(userId);
                noteId = noteService.save(note);
                if (noteId < 1) ra.addFlashAttribute("noteSaveError", true);
                else {
                    ra.addFlashAttribute("noteSaveSuccess", true);
                    ra.addFlashAttribute("notes", noteService.getAllNotesByUserId(user.getUserId()));
                }
            } else {
                note.setUserId(user.getUserId());
                int updated = noteService.update(note);
                if (updated > 0) {
                    ra.addFlashAttribute("noteUpdateSuccess", true);
                    ra.addFlashAttribute("notes", noteService.getAllNotesByUserId(user.getUserId()));
                } else {
                    ra.addFlashAttribute("noteUpdateError", true);
                }
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id, Authentication authentication,
                             RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        int deleted = noteService.deleteById(id);
        if (deleted < 0){
            logger.error("Note with id = " + id + " was not deleted");
            ra.addFlashAttribute("noteDeleteError", true);
        } else {
            ra.addFlashAttribute("noteDeleteSuccess", true);
        }
        ra.addFlashAttribute("files", noteService.getAllNotesByUserId(user.getUserId()));
        return "redirect:/home";
    }
}
