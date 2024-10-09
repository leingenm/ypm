package com.ypm.service.youtube;

import com.ypm.dto.BatchProcessingStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImportService {

    @Transactional
    List<String> importVideos(String playlistName, MultipartFile file);

    @Transactional
    List<String> importVideos(String playlistName, List<String> videosIds);

    BatchProcessingStatus checkProcessingStatus(String processingId);
}
