package com.ypm.controller;

import com.ypm.service.youtube.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class LibraryImportController {

    private final ImportService importService;

    @PostMapping("/watch-later")
    public ResponseEntity<String> importWatchLaterLibrary(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        importService.importVideos(file);

        return ResponseEntity.accepted().body("Videos are being processed. Please check back later.");
    }
}
