package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
                                   Authentication authentication, RedirectAttributes ra) {
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
            ra.addFlashAttribute("fileUploadError", true);
        } else {
            ra.addFlashAttribute("fileUploadSuccess", true);
        }
        ra.addFlashAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Integer id, Authentication authentication,
                                Model model, RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        int deleted = fileService.deleteById(id);
        if (deleted < 0){
            logger.error("File with id = " + id + " was not deleted");
            ra.addFlashAttribute("fileDeleteError", true);
        } else {
            ra.addFlashAttribute("fileDeleteSuccess", true);
        }
        ra.addFlashAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        return "redirect:/home";
    }

    @GetMapping("/download/{id}")
    public void getFile(@PathVariable("id") Integer id, HttpServletResponse response,
                        Model model) {
        File file = fileService.getFileById(id);
        if (file == null) {
            throw new IllegalArgumentException("There is no file with such id = " + id);
        } else {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(file.getFileData());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
