package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
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
        Integer userId = user.getUserId();
        if (!fileUpload.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileUpload.getOriginalFilename()));
            Long filesize = fileUpload.getSize();
            if (fileService.fileExists(filename,  userId, filesize)) {
                ra.addFlashAttribute("fileExists", true);
                return "redirect:/home#nav-files";
            }
            File uploadFile;
            int fileId = -1;
            try {
                uploadFile = new File(
                        null,
                        filename,
                        fileUpload.getContentType(),
                        filesize,
                        user.getUserId(),
                        fileUpload.getBytes(),
                        new Date(System.currentTimeMillis()));
                fileId = fileService.upload(uploadFile);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            if (fileId == -1) ra.addFlashAttribute("fileUploadError", true);
            else ra.addFlashAttribute("fileUploadSuccess", true);
        } else {
            ra.addFlashAttribute("fileUploadError", true);
        }
        ra.addFlashAttribute("files", fileService.getFilesByUserId(user.getUserId()));
        ra.addFlashAttribute("activeTab", "files");
        return "redirect:/home#nav-files";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable("id") Integer id, Authentication authentication,
                             RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        int deleted = fileService.deleteById(id, userId);
        if (deleted == 0){
            logger.error("File with id = " + id + " was not deleted");
            ra.addFlashAttribute("fileDeleteError", true);
        } else {
            ra.addFlashAttribute("fileDeleteSuccess", true);
        }
        ra.addFlashAttribute("files", fileService.getFilesByUserId(userId));
        ra.addFlashAttribute("activeTab", "files");
        return "redirect:/home#nav-files";
    }

    @GetMapping("/download/{id}")
    public void getFile(@PathVariable("id") Integer id, Authentication authentication,
                          HttpServletResponse response, RedirectAttributes ra) {
        User user = userService.getUserByUsername(authentication.getName());
        Integer userId = user.getUserId();
        File file = fileService.getFileById(id, userId);
        if (file == null) {
            ra.addFlashAttribute("fileDownloadError", true);
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
