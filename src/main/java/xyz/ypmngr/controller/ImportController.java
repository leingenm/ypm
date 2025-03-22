package xyz.ypmngr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.ypmngr.api.ImportApi;
import xyz.ypmngr.model.Playlist;
import xyz.ypmngr.service.ImportService;

@RestController
@RequiredArgsConstructor
public class ImportController implements ImportApi {

    private final ImportService importService;

    @Override
    public ResponseEntity<Playlist> importFromCsv(MultipartFile file) {
        return ResponseEntity.ok(importService.importFromCsv(file));
    }
}
