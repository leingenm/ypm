package com.ypm.service.youtube;

import com.ypm.dto.VideoImportDto;
import com.ypm.persistence.entity.Video;
import com.ypm.persistence.event.VideosSavedEvent;
import com.ypm.persistence.repository.PlaylistRepository;
import com.ypm.persistence.repository.VideoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final VideoRepository videoRepository;
    private final PlaylistRepository playlistRepository;
    private final EntityManager entityManager;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void importVideos(MultipartFile file) {
        var parsedVideos = this.parseCsv(file);
        var videos = this.saveVideosInBatches(parsedVideos);
    }

    public List<VideoImportDto> parseCsv(MultipartFile file) {
        List<VideoImportDto> videoImportDtos = new ArrayList<>();

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            var header = true;

            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                var data = line.split(",");
                var videoId = data[0];
                var videoTimeStamp = OffsetDateTime.parse(data[1]);
                var videoImportDto = new VideoImportDto(videoId, videoTimeStamp);
                videoImportDtos.add(videoImportDto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return videoImportDtos;
    }

    @Transactional
    public List<Video> saveVideosInBatches(List<VideoImportDto> videoImportDtos) {
        // DEV-NOTE: It's set to 50 as it's the maximum number of ids that YouTube API accepts
        final int batchSize = 50;
        final int size = videoImportDtos.size();

        final List<Video> videos = new ArrayList<>();

        final var watchLaterPlaylist = playlistRepository.findByName("Watch Later").orElseThrow();

        for (int i = 0; i < size; i += batchSize) {
            var videosBatch = videoImportDtos
                .subList(i, Math.min(i + batchSize, size))
                .stream()
                .map(dto -> new Video(dto.id(), dto.importDate().toLocalDate(), watchLaterPlaylist))
                .toList();

            videoRepository.saveAll(videosBatch);
            publisher.publishEvent(new VideosSavedEvent(videosBatch));
            entityManager.clear();

            videos.addAll(videosBatch);
        }

        return videos;
    }
}
