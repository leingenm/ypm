package xyz.ypmngr.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.ypmngr.model.Playlist;

public interface ImportService {

    Playlist importFromCsv(MultipartFile file);
}
