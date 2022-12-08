package com.example.springawsdocker.controllers;

import com.example.springawsdocker.services.FileStorageService;
import com.example.springawsdocker.vo.v1.UploadFileResponseVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/file/v1")
public class FileController {

   private Logger logger = Logger.getLogger(FileController.class.getName());

   @Autowired
   private FileStorageService service;

   @PostMapping("/uploadFile")
   public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file) {
      logger.info("Storing file to disk");

      var filename = service.storeFile(file);
      String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path("/file/v1/downloadFile/")
                                                          .path(filename)
                                                          .toUriString();

      return new UploadFileResponseVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
   }

   @PostMapping("/uploadMultipleFiles")
   public List<UploadFileResponseVO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
      logger.info("Storing files to disk");

      return Arrays.stream(files).map(this::uploadFile).collect(Collectors.toList());
   }

   @GetMapping("/downloadFile/{fileName:.+}")
   public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

      logger.info("Reading a file on disk");

      Resource resource = service.loadFileAsResource(fileName);

      String contentType = "";

      try {
         contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (Exception e) {
         logger.info("Could not determine file type");
      }

      if (contentType.isBlank()) contentType = "application/octet-stream";

      return ResponseEntity.ok()
                           .contentType(MediaType.parseMediaType(contentType))
                           .header(HttpHeaders.CONTENT_DISPOSITION,
                                   "attachment; filename=\""
                                   + resource.getFilename()
                                   + "\"")
                           .body(resource);
   }
}
