package com.ypm.service.youtube;

import com.ypm.constant.ProcessingStatus;
import com.ypm.dto.BatchProcessingStatus;
import com.ypm.dto.VideoImportDto;
import com.ypm.exception.PlayListNotFoundException;
import com.ypm.persistence.entity.Playlist;
import com.ypm.persistence.entity.Video;
import com.ypm.persistence.event.VideosSavedEvent;
import com.ypm.persistence.repository.PlaylistRepository;
import com.ypm.persistence.repository.VideoRepository;
import com.ypm.service.BatchStatusManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final VideoRepository videoRepository;
    private final PlaylistRepository playlistRepository;
    private final EntityManager entityManager;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public List<String> importVideos(String playlistName, MultipartFile file) {
        var parsedVideos = this.parseCsv(file);

        return this.saveVideosInBatches(playlistName, parsedVideos);
    }

    @Transactional
    @Override
    public List<String> importVideos(String playlistName, List<String> videosIds) {
        var videoImportDtos = videosIds.stream().map(videoId -> new VideoImportDto(videoId, OffsetDateTime.now()))
                                       .toList();

        return this.saveVideosInBatches(playlistName, videoImportDtos);
    }

    @Override
    public BatchProcessingStatus checkProcessingStatus(String processingId) {
        var batchProcessingStatuses = BatchStatusManager.getBatchProcessingStatuses();
        return batchProcessingStatuses.getOrDefault(processingId,
                                                    new BatchProcessingStatus(ProcessingStatus.NOT_FOUND));
    }

    @Transactional
    protected List<String> saveVideosInBatches(String playlistName, List<VideoImportDto> videoImportDtos) {
        // DEV-NOTE: It's set to 50 as it's the maximum number of ids that YouTube API accepts
        final int batchSize = 50;
        final int size = videoImportDtos.size();
        final List<String> processingIds = new ArrayList<>();

        final var playlist = playlistRepository.findByNameContainingIgnoreCase(playlistName).orElseThrow(
            () -> new PlayListNotFoundException(String.format("Playlist '%s' was not found", playlistName)));

        for (int i = 0; i < size; i += batchSize) {
            var processingId = UUID.randomUUID().toString();
            processingIds.add(processingId);

            var videosSubList = videoImportDtos.subList(i, Math.min(i + batchSize, size));
            this.processBatch(processingId, playlist, videosSubList);
        }

        return processingIds;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void processBatch(String processingId, Playlist playlist, List<VideoImportDto> videoImportDtoSubList) {
        try {
            BatchStatusManager.updateBatchStatus(processingId, ProcessingStatus.PROCESSING);

            var videosBatch = videoImportDtoSubList.stream()
                                                   .filter(dto -> isValidVideoId(dto.id()))
                                                   .map(dto -> videoRepository
                                                       .findVideoByYoutubeId(dto.id())
                                                       .orElseGet(() -> new Video(dto.id(), dto.importDate().toLocalDate(), playlist)))
                                                   .toList();

            videoRepository.saveAll(videosBatch);
            entityManager.flush();
            publisher.publishEvent(new VideosSavedEvent(processingId, videosBatch));
        } catch (RuntimeException e) {
            var failedVideoIds = videoImportDtoSubList.stream().map(VideoImportDto::id).toList();
            BatchStatusManager.updateBatchStatus(processingId, ProcessingStatus.FAILED, failedVideoIds);
            throw new RuntimeException(e);
        }
    }

    private List<VideoImportDto> parseCsv(MultipartFile file) {
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

                // Skip lines that do not have at least 2 columns
                if (data.length < 2) {
                    continue;
                }

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

    private boolean isValidVideoId(String youtubeId) {
        return youtubeId != null && !youtubeId.trim().isEmpty() && !youtubeId.equalsIgnoreCase("null");
    }
}
