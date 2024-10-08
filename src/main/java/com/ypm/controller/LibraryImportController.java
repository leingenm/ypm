package com.ypm.controller;

import com.ypm.persistence.entity.VideoImport;
import com.ypm.service.youtube.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class LibraryImportController {

    private final ImportService importService;

    @PostMapping("/watch-later")
    public ResponseEntity<String> importWatchLaterLibrary(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<VideoImport> savedVideos;
        savedVideos = importService.importCsv(file);

        var responseBody = String.format("Saved %s videos", savedVideos.size());
        return ResponseEntity.ok().body(responseBody);
    }
}
