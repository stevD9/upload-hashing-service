package me.stev.uploadservice.controller;

import me.stev.uploadservice.exception.HashesDoNotMatchException;
import me.stev.uploadservice.service.FileHashService;
import me.stev.uploadservice.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileHashService fileHashService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

        try {
            logger.info("Started uploading file...");
            fileStorageService.save(file);
            fileHashService.hashFile(file);
            logger.info("Saved file {} !!", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Saved file " + file.getOriginalFilename() + " !!");
        } catch (Exception e) {
            logger.error("Error uploading file!!, Error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Error uploading file!!");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("file") MultipartFile file,
                                         @RequestParam("id") Integer id) {
        try {
            logger.info("Started verifying file...");
            fileHashService.verifyFile(file, id);
            logger.info("Verification Passed!!!");
            return ResponseEntity.ok("Verification passed!!! Your files are identical");
        } catch (HashesDoNotMatchException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Hashed do not match!!! HAKKKK!!!!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("OOOOOPPPSSSS\n" + e.getMessage());
        }
    }
}
