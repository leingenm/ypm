package com.ypm.controller;

import com.ypm.constant.ProcessingStatus;
import com.ypm.dto.BatchProcessingStatus;
import com.ypm.service.youtube.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class LibraryImportController {

    private final ImportService importService;

    @PostMapping("/file")
    public ResponseEntity<?> importVideos(@RequestParam("playlist-name") String playlistName,
                                          @RequestParam("file") MultipartFile file) {
        var validationResponse =
            validateRequest(playlistName, file == null || file.isEmpty(), "File has no data or was not provided.");
        if (validationResponse != null) return validationResponse;

        var processingIds = importService.importVideos(playlistName, file);

        return ResponseEntity.accepted().body(processingIds);
    }

    @PostMapping
    public ResponseEntity<?> importVideos(@RequestParam("playlist-name") String playlistName,
                                          @RequestBody List<String> videosIds) {
        var validationResponse =
            validateRequest(playlistName, videosIds.isEmpty(), "Videos IDs were not provided");
        if (validationResponse != null) return validationResponse;

        var processingId = importService.importVideos(playlistName, videosIds);

        return ResponseEntity.accepted().body(processingId);
    }

    @GetMapping("/status/{processing-id}")
    public ResponseEntity<BatchProcessingStatus> checkStatus(@PathVariable("processing-id") String processingId) {
        var processingStatus = importService.checkProcessingStatus(processingId);

        if (processingStatus.getStatus() == ProcessingStatus.COMPLETED) {
            return ResponseEntity.ok(processingStatus);
        } else if (processingStatus.getStatus() == ProcessingStatus.FAILED) {
            return ResponseEntity.internalServerError().body(processingStatus);
        } else {
            return ResponseEntity.accepted().body(processingStatus);
        }
    }

    private static ResponseEntity<String> validateRequest(String playlistName, boolean isNullOrEmpty, String errorMessage) {
        if (playlistName == null || playlistName.isEmpty()) {
            return ResponseEntity.badRequest().body("Playlist name was not provided");
        }

        if (isNullOrEmpty) {
            return ResponseEntity.badRequest().body(errorMessage);
        }

        return null;
    }
}
