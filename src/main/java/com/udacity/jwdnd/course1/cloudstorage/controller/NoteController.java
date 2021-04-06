package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final UserService userService;

    private final MessageSource messageSource;

    public NoteController(NoteService noteService, UserService userService, MessageSource messageSource) {
        this.noteService = noteService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @PostMapping("/save")
    public String saveNote(@ModelAttribute("note") Note note, Authentication authentication,
                           RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        String errorMsg;
        String successMsg = null;
        //if note form is empty
        if (note == null) {
            errorMsg = messageSource.getMessage("notes-tab.note-empty-form-msg", null, Locale.getDefault());
            ra.addFlashAttribute("errorMessage", errorMsg);
            logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
            return "redirect:/home#nav-notes";

        } //note description length check
        else if (note.getNoteDescription().length() > 1000) {
            errorMsg = messageSource.getMessage("notes-tab.note-description-too-long-msg", null, Locale.getDefault());
            ra.addFlashAttribute("errorMessage", errorMsg);
            logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
            return "redirect:/home#nav-notes";

        } //if the same note submitted again from the same user
        else if (noteService.exists(note, userId)) {
            errorMsg = messageSource.getMessage("notes-tab.note-already-exists-msg", null, Locale.getDefault());
            ra.addFlashAttribute("errorMessage", errorMsg);
            logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
            return "redirect:/home#nav-notes";
        }

        if (note.getNoteId() == null) {
            note.setUserId(userId);
            int noteSaved = noteService.save(note);

            //if note was not saved for some reason
            if (noteSaved != 1) {
                errorMsg = messageSource.getMessage("notes-tab.note-save-error-msg", null, Locale.getDefault());
                ra.addFlashAttribute("errorMessage", errorMsg);
                logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
            } else {
                successMsg = messageSource.getMessage("notes-tab.note-save-success-msg", null, Locale.getDefault());
                ra.addFlashAttribute("successMessage", successMsg);
                ra.addFlashAttribute("notes", noteService.getAllNotesByUserId(user.getUserId()));
            }
        } else {
            note.setUserId(user.getUserId());
            int updated = noteService.update(note, userId);
            if (updated != 1) {
                errorMsg = messageSource.getMessage("notes-tab.note-update-error-msg", null, Locale.getDefault());
                ra.addFlashAttribute("errorMessage", errorMsg);
                logger.debug("Note was not saved: " + errorMsg + " from user: " + userId);
            } else {
                successMsg = messageSource.getMessage("notes-tab.note-update-success-msg", null, Locale.getDefault());
                ra.addFlashAttribute("successMessage", successMsg);
                ra.addFlashAttribute("notes", noteService.getAllNotesByUserId(user.getUserId()));
            }
        }
        return "redirect:/home#nav-notes";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id, Authentication authentication, RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        int deleted = noteService.deleteById(id, userId);
        if (deleted == 0){
            logger.error("Note with id = " + id + " was not deleted");
            String errorMsg = messageSource.getMessage("notes-tab.note-delete-error-msg", null, Locale.getDefault());
            ra.addFlashAttribute("errorMessage", errorMsg);
        } else {
            String successMsg = messageSource.getMessage("notes-tab.note-delete-success-msg", null, Locale.getDefault());
            ra.addFlashAttribute("successMessage", successMsg);
        }
        ra.addFlashAttribute("files", noteService.getAllNotesByUserId(user.getUserId()));
        return "redirect:/home#nav-notes";
    }
}
