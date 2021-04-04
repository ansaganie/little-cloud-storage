package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.StatusMessage;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Date;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService= userService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                   Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        File uploadFile = null;
        int fileId = -1;
        try {
            uploadFile = new File(
                    null,
                    StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename())),
                    fileUpload.getContentType(),
                    fileUpload.getSize(),
                    user.getUserId(),
                    fileUpload.getBytes(),
                    new Date(System.currentTimeMillis()));
            fileId = fileService.upload(uploadFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        System.out.println(uploadFile);
        System.out.println(fileId);
        if(fileId == -1) {
            model.addAttribute("fileUploadError", true);
        } else {
            model.addAttribute("fileUploadSuccess", true);
        }
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Integer id, Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName());
        int deleted = fileService.deleteById(id);
        if (deleted <= 0) logger.error("File with id = " + id + " was not deleted");
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/download/{id}")
    public String getFile(@PathVariable("id") Integer id) {
        File file = fileService.getFileById(id);
        return "redirect:/home";
    }
}
