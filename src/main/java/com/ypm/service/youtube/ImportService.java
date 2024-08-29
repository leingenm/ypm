package com.ypm.service.youtube;

import com.ypm.persistence.entity.VideoImport;
import com.ypm.persistence.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final VideoRepository videoRepository;

    @Transactional
    public List<VideoImport> importCsv(MultipartFile file) throws IOException {
        final List<VideoImport> videos = new ArrayList<>();

        // Parse CSV
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            var header = true;

            while ((line = reader.readLine()) != null ) {
                if (header) {
                    header = false;
                    continue;
                }

                var data = line.split(",");
                var videoId = data[0];
                var videoTimeStamp = data[1];
                var video = new VideoImport(videoId, videoTimeStamp);
                videos.add(video);
            }
        }

        // Save into DB
        return videoRepository.saveAll(videos);
    }
}
