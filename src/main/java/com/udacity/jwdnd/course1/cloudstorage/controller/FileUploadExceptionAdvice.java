package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    private final MessageSource messageSource;
    private final String ERROR_MESSAGE = "fileErrorMessage";

    public FileUploadExceptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes ra) {
        ra.addFlashAttribute(ERROR_MESSAGE, messageSource.getMessage(
                "files-tab.file-upload-size-error-msg", null, Locale.getDefault()
        ));
        return "redirect:/home#nav-files";
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public String handleFileSizeException(RedirectAttributes ra) {
        ra.addFlashAttribute(ERROR_MESSAGE, messageSource.getMessage(
                "files-tab.file-upload-size-error-msg", null, Locale.getDefault()
        ));
        return "redirect:/home#nav-files";
    }
}
